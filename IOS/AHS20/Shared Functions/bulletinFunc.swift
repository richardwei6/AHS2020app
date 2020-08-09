//
//  bulletinFunc.swift
//  AHS20
//
//  Created by Richard Wei on 7/12/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import SystemConfiguration
import FirebaseDatabase
import SDWebImage

var bulletinReadDict = [String : Bool](); // read = true
var totalArticles = [bulletinArticleData]();

func loadBullPref(){
    if let data = UserDefaults.standard.data(forKey: "bulletinReadDict"){
        do{
            bulletinReadDict = try JSONDecoder().decode([String: Bool].self, from: data);
        }catch{
            print("error decoding");
        }
    }
}

func saveBullPref(){
    do{
        
        var currDict = [String : Bool]();
        for i in totalArticles{
            currDict[i.articleID ?? ""] = true;
        }
        
        var save = [String : Bool]();
        for i in bulletinReadDict{
            if (currDict[i.key] == true){
                save[i.key] = true;
            }
        }
        
        let data = try JSONEncoder().encode(save);
        UserDefaults.standard.set(data, forKey: "bulletinReadDict");
        
    }catch{
        print("error encoding")
    }
}
