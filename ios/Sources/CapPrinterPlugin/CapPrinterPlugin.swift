import Foundation
import Capacitor

@objc(CapPrinterPlugin)
public class CapPrinterPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "CapPrinterPlugin"
    public let jsName = "CapPrinter"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "print", returnType: CAPPluginReturnPromise)
    ]
    
    @objc func print(_ call: CAPPluginCall) {
    // Check for a local file first
    if let localPath = call.getString("localPath"), !localPath.isEmpty {
        // Create a file URL from the local path
        let fileURL = URL(fileURLWithPath: localPath)
        do {
            let pdfData = try Data(contentsOf: fileURL)
            DispatchQueue.main.async {
                if UIPrintInteractionController.canPrint(pdfData) {
                    let printController = UIPrintInteractionController.shared
                    let printInfo = UIPrintInfo(dictionary: nil)
                    printInfo.jobName = fileURL.lastPathComponent
                    printInfo.outputType = .general
                    printController.printInfo = printInfo
                    printController.printingItem = pdfData

                    printController.present(animated: true) { _, completed, error in
                        if let error = error {
                            call.reject("Print error: \(error.localizedDescription)")
                        } else if completed {
                            call.resolve()
                        } else {
                            call.reject("Print was cancelled or failed.")
                        }
                    }
                } else {
                    call.reject("These PDF data cannot be printed.")
                }
            }
        } catch {
            call.reject("Error loading local PDF file: \(error.localizedDescription)")
        }
        return
    }
    
    // Otherwise, check for URL
    guard let urlString = call.getString("url"),
          let pdfURL = URL(string: urlString) else {
        call.reject("No valid URL or local file path provided")
        return
    }
    
    // Download the PDF asynchronously
    let task = URLSession.shared.dataTask(with: pdfURL) { data, response, error in
        if let error = error {
            call.reject("Error downloading PDF: \(error.localizedDescription)")
            return
        }
        
        guard let pdfData = data else {
            call.reject("No data received")
            return
        }
        
        DispatchQueue.main.async {
            if UIPrintInteractionController.canPrint(pdfData) {
                let printController = UIPrintInteractionController.shared
                let printInfo = UIPrintInfo(dictionary: nil)
                printInfo.jobName = pdfURL.lastPathComponent
                printInfo.outputType = .general
                printController.printInfo = printInfo
                printController.printingItem = pdfData
                
                printController.present(animated: true) { _, completed, error in
                    if let error = error {
                        call.reject("Print error: \(error.localizedDescription)")
                    } else if completed {
                        call.resolve() // Success
                    } else {
                        call.reject("Print was cancelled or failed.")
                    }
                }
            } else {
                call.reject("These PDF data cannot be printed.")
            }
        }
    }
    task.resume()
}

}
