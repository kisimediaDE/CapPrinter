import Foundation

@objc public class CapPrinter: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
