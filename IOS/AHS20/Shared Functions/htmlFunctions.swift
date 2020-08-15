//
//  htmlFunctions.swift
//  AHS20
//
//  Created by Richard Wei on 7/15/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

func parseHTML(s: String) -> NSMutableAttributedString{
    let htmlData = NSString(string: s).data(using: String.Encoding.unicode.rawValue);
    let options = [NSAttributedString.DocumentReadingOptionKey.documentType:
        NSAttributedString.DocumentType.html];
    do{
        let t = try NSMutableAttributedString(data: htmlData ?? Data(), options: options, documentAttributes: nil);
        t.addAttribute(NSAttributedString.Key.font, value: UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize)) as Any, range: NSMakeRange(0, t.length));
        return t;
    }
    catch let error{
        print(error);
        return NSMutableAttributedString(string: s);
    }
}
