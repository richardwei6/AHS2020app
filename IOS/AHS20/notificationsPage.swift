//
//  notificationsPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/24/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import Firebase
import FirebaseDatabase

class notificationsClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {


    @IBOutlet weak var notificationScrollView: UIScrollView!
    
    @IBOutlet weak var noNotificationLabel: UILabel!

    
    var articleContentInSegue: articleData?;
    
    func getLocalNotifications(){
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
                    
                    singleNotification.messageID = article.key as! String;
                    
                    
                    while let notificationContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                        
                        if (notificationContent.key == "notificationArticleID"){
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
                        else if (notificationContent.key == "notificationCategory"){
                            singleNotification.notificationCatagory = notificationContent.value as? Int;
                        }
                        
                    }
                    totalNotificationList.append(singleNotification);
                    filterTotalArticles();
                    self.loadScrollView();
                    self.refreshControl.endRefreshing();
                };
            }
        }
        else{
            //setUpAllViews();
            print("no network detected - notifications");
            self.refreshControl.endRefreshing();
        }
    }
    
    @objc func openArticle(_ sender: notificationUIButton) {
        /*if (sender.unreadBool == true){
            unreadNotificationSize-=1;
            readNotificationSize+=1;
            sender.unreadBool = false;
        }*/
        if (sender.alreadyRead == false){
            notificationList[0].append(notificationList[1][sender.articleIndex]);
            notificationList[1].remove(at: sender.articleIndex);
          //  saveNotificationPref();
            notificationReadDict[sender.notificationCompleteData.messageID ?? ""] = true;
            saveNotifPref();
        }
        
        findArticle(id: sender.notificationCompleteData.notificationArticleID ?? "");
        performSegue(withIdentifier: "notificationToArticle", sender: nil);
        loadScrollView();
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "notificationToArticle"){
            let vc = segue.destination as! articlePageViewController;
            vc.articleContent = articleContentInSegue;
        }
    }
    
    func findArticle(id: String){
        if (id == ""){
            return;
        }
        for j in homeArticleList{
            for i in j{
                if (id == i.articleID){
                    articleContentInSegue = i;
                    return;
                }
            }
        }
        for j in bulletinArticleList{
            for i in j{
                if (id == i.articleID){
                    articleContentInSegue = bulletinDataToarticleData(data: i);
                    return;
                }
            }
        }
    }
    
    var unreadNotificationSize = 0;
    var readNotificationSize = 0;
    
    var notificationFrame = CGRect(x: 0, y: 0, width: 0, height: 0);
    
    var horizontalPadding = CGFloat(10);
    var verticalPadding = CGFloat(10);
    
    let timeStampLength = CGFloat(100);
    
    var refreshControl = UIRefreshControl();

    func typeIDToString(id: Int) -> String{
        if (id == 0){
            return "Alerts";
        }
        else if (id == 1){
            return "Sports";
        }
        else if (id == 2){
            return "ASB";
        }
        else if (id == 3){
            return "District";
        }
        else{
            return "Bulletin";
        }
    }
    
    func loadScrollView(){
        
        unreadNotificationSize = notificationList[1].count;
        readNotificationSize = notificationList[0].count;
        
        // remove prev subviews
        for subview in notificationScrollView.subviews{
            subview.removeFromSuperview();
        }
        
        
        if (unreadNotificationSize != 0 || readNotificationSize != 0){
            
            notificationScrollView.isHidden = false;
            noNotificationLabel.isHidden = true;
            
            notificationFrame.size.width = UIScreen.main.bounds.size.width - (2 * horizontalPadding);
            notificationFrame.size.height = 130;
            // add notification label at start
            for nIndex in 0..<unreadNotificationSize{
                notificationFrame.origin.x = horizontalPadding;
                notificationFrame.origin.y = verticalPadding+((notificationFrame.size.height + verticalPadding)*CGFloat(nIndex));
                
                let notificationButton = notificationUIButton(frame: notificationFrame);
                let currNotif = notificationList[1][nIndex]; // TODO: import data
                
                let chevronWidth = CGFloat(22);
                let chevronFrame = CGRect(x: notificationFrame.size.width-chevronWidth-15, y: (notificationFrame.size.height/2)-(chevronWidth/2), width: chevronWidth-5, height: chevronWidth);
                let chevronImage = UIImageView(frame: chevronFrame);
                chevronImage.image = UIImage(systemName: "chevron.right");
                chevronImage.tintColor = UIColor.gray;
                
                let notificationCatagoryLabelHeight = CGFloat(20);
                let notificationCatagoryLabelFrame = CGRect(x: 10, y: 12, width: 65, height: notificationCatagoryLabelHeight);
                let notificationCatagoryLabel = UILabel(frame: notificationCatagoryLabelFrame);
                notificationCatagoryLabel.backgroundColor = makeColor(r: 159, g: 12, b: 12);
                notificationCatagoryLabel.text = typeIDToString(id: currNotif.notificationCatagory ?? 0);
                notificationCatagoryLabel.textAlignment = .center;
                notificationCatagoryLabel.textColor = UIColor.white;
                notificationCatagoryLabel.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                notificationCatagoryLabel.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                 //SFProText-Bold, SFProDisplay-Regular, SFProDisplay-Semibold, SFProDisplay-Black
                
                let readLabelFrame = CGRect(x: 15 + notificationCatagoryLabelFrame.size.width, y: 12, width: 40, height: notificationCatagoryLabelHeight);
                let readLabel = UILabel(frame: readLabelFrame);
                readLabel.text = "New";
                readLabel.backgroundColor = UIColor.systemYellow;
                readLabel.textAlignment = .center;
                readLabel.textColor = UIColor.white;
                readLabel.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                readLabel.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                
                let notificationTitleFrame = CGRect(x: 10, y: notificationCatagoryLabelHeight + 15, width: notificationFrame.size.width - chevronWidth - timeStampLength, height: 30);
                let notificationTitle = UILabel(frame: notificationTitleFrame);
                notificationTitle.text = currNotif.notificationTitle;
                notificationTitle.font = UIFont(name:"SFProText-Bold",size: 18);
                notificationTitle.adjustsFontSizeToFitWidth = true;
                notificationTitle.minimumScaleFactor = 0.2;
                
                let notificationBodyFrame = CGRect(x: 10, y: notificationTitleFrame.size.height + 10 + notificationCatagoryLabelFrame.size.height, width: notificationFrame.size.width  - chevronWidth - 27, height: notificationFrame.size.height - (notificationCatagoryLabel.frame.size.height + 5 + notificationTitle.frame.size.height + 5));
                let notificationBodyText = UILabel(frame: notificationBodyFrame);
                notificationBodyText.text = currNotif.notificationBody;
                notificationBodyText.numberOfLines = 3;
                notificationBodyText.font = UIFont(name:"SFProDisplay-Regular",size: 14);
                
                
                let timeStampFrame = CGRect(x: notificationFrame.size.width - chevronWidth - timeStampLength + 10, y: 5, width: timeStampLength, height: 30);
                let timeStamp = UILabel(frame: timeStampFrame);
                timeStamp.text = epochClass.epochToString(epoch: currNotif.notificationUnixEpoch ?? -1);
                timeStamp.font = UIFont(name:"SFProDisplay-Regular",size: 12);
                timeStamp.textAlignment = .right;
                timeStamp.textColor = UIColor.darkGray;
                
                notificationButton.backgroundColor = UIColor.white;
                
                notificationButton.layer.shadowColor = UIColor.black.cgColor;
                notificationButton.layer.shadowOpacity = 0.2;
                notificationButton.layer.shadowRadius = 5;
                notificationButton.layer.shadowOffset = CGSize(width: 0 , height:3);
                
                notificationButton.notificationCompleteData = currNotif;
                notificationButton.articleIndex = nIndex;
                
                notificationButton.addSubview(readLabel);
                notificationButton.addSubview(notificationCatagoryLabel);
                notificationButton.addSubview(chevronImage);
                notificationButton.addSubview(notificationTitle);
                notificationButton.addSubview(notificationBodyText);
                notificationButton.addSubview(timeStamp);
                
                notificationButton.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
                notificationScrollView.addSubview(notificationButton);
            }
            for nIndex in 0..<readNotificationSize{
                notificationFrame.origin.x = horizontalPadding;
                notificationFrame.origin.y = verticalPadding+((notificationFrame.size.height + verticalPadding)*CGFloat(nIndex + unreadNotificationSize));
                
                let notificationButton = notificationUIButton(frame: notificationFrame);
                let currNotif = notificationList[0][nIndex];
                
                let chevronWidth = CGFloat(22);
                let chevronFrame = CGRect(x: notificationFrame.size.width-chevronWidth-15, y: (notificationFrame.size.height/2)-(chevronWidth/2), width: chevronWidth-5, height: chevronWidth);
                let chevronImage = UIImageView(frame: chevronFrame);
                chevronImage.image = UIImage(systemName: "chevron.right");
                chevronImage.tintColor = UIColor.gray;
                
                let notificationCatagoryLabelHeight = CGFloat(20);
                let notificationCatagoryLabelFrame = CGRect(x: 10, y: 12, width: 65, height: notificationCatagoryLabelHeight);
                let notificationCatagoryLabel = UILabel(frame: notificationCatagoryLabelFrame);
                notificationCatagoryLabel.backgroundColor = UIColor.gray;
                notificationCatagoryLabel.layer.cornerRadius = 5;
                notificationCatagoryLabel.text = typeIDToString(id: currNotif.notificationCatagory ?? 0);
                notificationCatagoryLabel.textAlignment = .center;
                notificationCatagoryLabel.textColor = UIColor.white;
                notificationCatagoryLabel.font = UIFont(name: "SFProDisplay-Black", size: 12);
                notificationCatagoryLabel.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                 //SFProText-Bold, SFProDisplay-Regular, SFProDisplay-Semibold, SFProDisplay-Black
                
                let notificationTitleFrame = CGRect(x: 10, y: notificationCatagoryLabelHeight + 15, width: notificationFrame.size.width - chevronWidth - timeStampLength, height: 30);
                let notificationTitle = UILabel(frame: notificationTitleFrame);
                notificationTitle.text = currNotif.notificationTitle;
                notificationTitle.font = UIFont(name:"SFProText-Bold",size: 18);
                notificationTitle.adjustsFontSizeToFitWidth = true;
                notificationTitle.minimumScaleFactor = 0.2;
                
                let notificationBodyFrame = CGRect(x: 10, y: notificationTitleFrame.size.height + 10 + notificationCatagoryLabelFrame.size.height, width: notificationFrame.size.width  - chevronWidth - 27, height: notificationFrame.size.height - (notificationCatagoryLabel.frame.size.height + 5 + notificationTitle.frame.size.height + 5));
                let notificationBodyText = UILabel(frame: notificationBodyFrame);
                notificationBodyText.text = currNotif.notificationBody;
                notificationBodyText.numberOfLines = 3;
                notificationBodyText.font = UIFont(name:"SFProDisplay-Regular",size: 14);
                
                
                let timeStampFrame = CGRect(x: notificationFrame.size.width - chevronWidth - timeStampLength + 10, y: 5, width: timeStampLength, height: 30);
                let timeStamp = UILabel(frame: timeStampFrame);
                timeStamp.text = epochClass.epochToString(epoch: currNotif.notificationUnixEpoch ?? -1);
                timeStamp.font = UIFont(name:"SFProDisplay-Regular",size: 12);
                timeStamp.textAlignment = .right;
                timeStamp.textColor = UIColor.darkGray;
                
                notificationButton.backgroundColor = makeColor(r: 250, g: 250, b: 250);
                
                notificationButton.layer.shadowColor = UIColor.black.cgColor;
                notificationButton.layer.shadowOpacity = 0.2;
                notificationButton.layer.shadowRadius = 5;
                notificationButton.layer.shadowOffset = CGSize(width: 0 , height:3);
                
                notificationButton.alreadyRead = true;
                notificationButton.notificationCompleteData = currNotif;
                notificationButton.articleIndex = nIndex+unreadNotificationSize;
                
                notificationButton.addSubview(notificationCatagoryLabel);
                notificationButton.addSubview(chevronImage);
                notificationButton.addSubview(notificationTitle);
                notificationButton.addSubview(notificationBodyText);
                notificationButton.addSubview(timeStamp);
                
                notificationButton.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
                notificationScrollView.addSubview(notificationButton);
            }
            notificationScrollView.contentSize = CGSize(width: notificationFrame.size.width, height: (2*verticalPadding)+((notificationFrame.size.height+verticalPadding)*CGFloat(unreadNotificationSize+readNotificationSize)));
            notificationScrollView.delegate = self;
        }
        else{
            notificationScrollView.isHidden = true;
            noNotificationLabel.isHidden = false;
        }
        
        notificationScrollView.addSubview(refreshControl);
        notificationScrollView.isScrollEnabled = true;
        notificationScrollView.alwaysBounceVertical = true;
    }
    
    @objc func refreshNotifications(){
        // implement get data
      //  loadNotificationPref();
        loadNotifPref();
        getLocalNotifications();
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        // set iphone x or above color below the safe area
        
        notificationScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        totalNotificationList = [notificationData]();
        notificationList = [[notificationData]](repeating: [notificationData](), count: 2);
        
        refreshControl.addTarget(self, action: #selector(refreshNotifications), for: UIControl.Event.valueChanged);
        notificationScrollView.addSubview(refreshControl);
        notificationScrollView.isScrollEnabled = true;
        notificationScrollView.alwaysBounceVertical = true;
        loadNotifPref();
        getLocalNotifications();
        
    }
    
    
    @IBAction func exitPopup(_ sender: UIButton) {
        unreadNotif = (notificationList[1].count > 0);
        dismiss(animated: true);
    }
    
}
