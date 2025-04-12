import Foundation
import Capacitor

@objc(CapPrinterPlugin)
public class CapPrinterPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "CapPrinterPlugin"
    public let jsName = "CapPrinter"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "print", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "isAvailable", returnType: CAPPluginReturnPromise)
    ]
    
    @objc func print(_ call: CAPPluginCall) {
        let options = call.getObject("options") ?? [:]
        let outputTypeStr = options["outputType"] as? String ?? "general"
        let orientationStr = options["orientation"] as? String ?? "portrait"
        let duplexEnabled = options["duplex"] as? Bool ?? false

        let outputType: UIPrintInfo.OutputType
        switch outputTypeStr {
        case "photo": outputType = .photo
        case "grayscale": outputType = .grayscale
        default: outputType = .general
        }

        let orientation: UIPrintInfo.Orientation = (orientationStr == "landscape") ? .landscape : .portrait
        let duplex: UIPrintInfo.Duplex = duplexEnabled ? .longEdge : .none

        func startPrintJob(with data: Data, jobName: String) {
            DispatchQueue.main.async {
                if UIPrintInteractionController.canPrint(data) {
                    let controller = UIPrintInteractionController.shared
                    let printInfo = UIPrintInfo(dictionary: nil)
                    printInfo.jobName = jobName
                    printInfo.outputType = outputType
                    printInfo.orientation = orientation
                    printInfo.duplex = duplex
                    controller.printInfo = printInfo
                    controller.printingItem = data

                    controller.present(animated: true) { _, completed, error in
                        if let error = error {
                            call.reject("Print error: \(error.localizedDescription)")
                        } else if completed {
                            call.resolve()
                        } else {
                            call.reject("Print was cancelled or failed.")
                        }
                    }
                } else {
                    call.reject("This PDF cannot be printed.")
                }
            }
        }

        // Local path
        if let localPath = call.getString("localPath"), !localPath.isEmpty {
            let fileURL = URL(fileURLWithPath: localPath)
            do {
                let data = try Data(contentsOf: fileURL)
                startPrintJob(with: data, jobName: fileURL.lastPathComponent)
            } catch {
                call.reject("Failed to load local PDF: \(error.localizedDescription)")
            }
            return
        }

        // Remote URL
        guard let urlStr = call.getString("url"),
              let url = URL(string: urlStr) else {
            call.reject("No valid URL or local file path provided")
            return
        }

        let task = URLSession.shared.dataTask(with: url) { data, _, error in
            if let error = error {
                call.reject("Failed to download PDF: \(error.localizedDescription)")
                return
            }
            guard let data = data else {
                call.reject("No data received")
                return
            }
            startPrintJob(with: data, jobName: url.lastPathComponent)
        }
        task.resume()
    }

    @objc func isAvailable(_ call: CAPPluginCall) {
        call.resolve(["available": true])
    }
}
