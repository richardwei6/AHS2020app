//
//  hrefFunctions.swift
//  AHS20
//
//  Created by Richard Wei on 7/15/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation


func parseHref(h: String) -> (String, URL){ // txt, https://
    var i = 0;
    var p = false, content = false;
    var a:String = "", b:String = "";
    while i < h.count{
        if (h[i] == "\""){
            p = !p;
            if (p == false){
                content = true;
            }
        }
        else if (p == true){
            a += h[i];
        }
        else if (content == true){
            if (h[i] == "<"){
                content = false;
            }
            else if (h[i] != ">"){
                b += h[i];
            }
        }
        i+=1;
    }
    return (b, (URL(string: "\(a)") ?? URL(string: "https://ahs.ausd.net")!) as URL); // change ! to ??
}

func replaceString(t: String) -> (String, [((Int, Int),URL)]) { // replace all <a href = "https://">txt</a> with txt
    var out = [((Int, Int), URL)]();
    var s = t;
    var i = 0;
    while i < s.count{
        if (s[i] == "<"){
            var j = i;
            while (j < s.count){ // find end
                if (s[j] == ">" && s[j-1] == "a"){
                    break;
                }
                j+=1;
            }
            let m = parseHref(h: s[i..<j+1]);
            // replace [i,j) with m.0
            let range = s.index(s.startIndex, offsetBy: i)..<s.index(s.startIndex, offsetBy: j+1);
           // print("old string - \(s)")
            s.replaceSubrange(range, with: m.0);
          //  print("new string - \(s) with - \(i) to \(i+m.0.count-1)");
            out.append(((i, i + m.0.count-1), m.1));
            i += m.0.count;
        }
        else{
            i+=1;
        }
    }
    return (s, out);
}

func parseHTML(s: String) -> NSMutableAttributedString{
    let t = replaceString(t: s);
    //print("final string - \(t.0)")
    let out = NSMutableAttributedString(string: t.0);
    for i in t.1{
    //    print("final ranges - \(i.0.0) to \(i.0.1) : \(out.length)")
        out.setAttributes([.link: i.1], range: NSRange(location: i.0.0, length: i.0.1-i.0.0+1));
    }
    return out;
}
