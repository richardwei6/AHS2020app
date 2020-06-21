//
//  notificationFunc.swift
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


// NOTIFICATION START
var totalNotificationList = [notificationData]();
var notificationReadDict = [String : Bool](); // Message ID : Read = true
var notificationList = [[notificationData]](repeating: [notificationData](), count: 2);

func getNotificationData(){
    setUpConnection();
    if (internetConnected){
        print("ok -------- loading articles - notifications");
        //print(s);
        totalNotificationList = [notificationData]();
        ref.child("notifications").observeSingleEvent(of: .value) { (snapshot) in
            let enumerator = snapshot.children;
            while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                let enumerator = article.children;
                var singleNotification = notificationData();
                while let notificationContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                    
                    if (notificationContent.key == "messageID"){
                        singleNotification.messageID = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationArticleID"){
                        singleNotification.notificationArticleID  = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationBody"){
                        singleNotification.notificationBody  = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationTitle"){
                        singleNotification.notificationTitle  = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationUnixEpoch"){
                        singleNotification.notificationUnixEpoch  = notificationContent.value as? Int64;
                    }
                    
                }
                totalNotificationList.append(singleNotification);
                filterTotalArticles();
            };
        }
    }
    else{
        //setUpAllViews();
        print("no network detected - notifications");
    }
}

func loadNotifPref(){
    if let data = UserDefaults.standard.data(forKey: "notificationReadDict"){
        do{
            let decoder = JSONDecoder();
            notificationReadDict = try decoder.decode([String:Bool].self, from: data);
        }catch{
            print("error decoding")
        }
    }
    else{
        print("default - no saved found");
    }
}

func saveNotifPref(){
    do{
        
        // combine only stuff that exists
        let encoder = JSONEncoder();
        let data = try encoder.encode(notificationReadDict);
        UserDefaults.standard.set(data, forKey: "notificationReadDict");
        loadNotifPref();
    } catch{
        print("error encoding object to save");
    }
}

func filterTotalArticles(){ // inital
    notificationList = [[notificationData]](repeating: [notificationData](), count: 2);
    for notification in totalNotificationList{
        notificationList[(notificationReadDict[notification.messageID ?? ""] == true ? 0 : 1)].append(notification);
    }
    unreadNotif = (notificationList[1].count > 0);
}

// NOTIFICATION END
