//
//  savedArticleClass.swift
//  AHS20
//
//  Created by Richard Wei on 6/20/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import SystemConfiguration
import FirebaseDatabase
import SDWebImage

func sortArticlesByTime(a: articleData, b: articleData)->Bool{
    let currTime = Int64(NSDate().timeIntervalSince1970);
    if (a.articleUnixEpoch ?? INT64_MAX > currTime && b.articleUnixEpoch ?? INT64_MAX > currTime){
        return (a.articleUnixEpoch ?? INT64_MAX) < (b.articleUnixEpoch ?? INT64_MAX);
    }
    else{
        return (a.articleUnixEpoch ?? INT64_MAX) > (b.articleUnixEpoch ?? INT64_MAX);
    }
}

final class savedArticleClass{
    static var savedArticles = [String: articleData]();
    class func getSavedArticles() -> [articleData]{ // get saved articles from phone local storage
        //savedArticles = ( UserDefaults.standard.dictionary(forKey: "savedArticles") ?? [String: articleData]() ) as! [String : articleData];
        getArticleDictionary();
        var temp = [articleData]();
        for i in savedArticles{
            temp.append(i.value);
        }
        return temp.sorted(by: sortArticlesByTime);
    }
    class func saveCurrArticle(articleID: String, article: articleData){
        savedArticles[articleID] = article;
        saveArticleDictionary();
    }
    class func removeCurrArticle(articleID: String){
        savedArticles[articleID] = nil;
        saveArticleDictionary();
    }
    
    class func isSavedCurrentArticle(articleID: String) -> Bool{
        return savedArticles[articleID] != nil;
    }
    // custom object saving
    class func saveArticleDictionary(){
        do{
            let encoder = JSONEncoder();
            let data = try encoder.encode(savedArticles);
            UserDefaults.standard.set(data, forKey: "savedArticleDict");
        } catch{
            print("error encoding object to save");
        }
    }
    class func getArticleDictionary(){
        if let data = UserDefaults.standard.data(forKey: "savedArticleDict"){
            do{
                let decoder = JSONDecoder();
                
                savedArticles = try decoder.decode([String:articleData].self, from: data);
                
            }catch{
                print("error decoding")
            }
        }
        else{
            print("default - no saved found");
        }
    }
}
