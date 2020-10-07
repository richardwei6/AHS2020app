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

class notificationsClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate, UIViewControllerTransitioningDelegate {
    
    
    @IBOutlet weak var notificationScrollView: UIScrollView!
    
    @IBOutlet weak var noNotificationLabel: UILabel!
    
    var articleDictionary = [String: articleData]();
    
    var articleContentInSegue: articleData?;
    
    
    let interactor = Interactor();
    let transition = CATransition();
    
    func transition(to controller: UIViewController) {
        transition.duration = 0.2
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromRight
        view.window?.layer.add(transition, forKey: kCATransition)
        present(controller, animated: false)
    }
    
    func animationController(
        forDismissed dismissed: UIViewController)
        -> UIViewControllerAnimatedTransitioning? {
            
            return DismissAnimator()
    }
    
    func interactionControllerForDismissal(
        using animator: UIViewControllerAnimatedTransitioning)
        -> UIViewControllerInteractiveTransitioning? {
            
            return interactor.hasStarted
                ? interactor
                : nil
    }

    /*@objc func articleSelector(notification: NSNotification){ // instigate transition
        guard let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(identifier: "articlePageController") as? articlePageViewController else{
            return;
        };
        vc.transitioningDelegate = self;
        vc.interactor = interactor;
        vc.articleContent = notification.userInfo?["articleContent"] as? articleData;
        transition(to: vc);
    }*/
    
    @objc func openArticle(_ sender: notificationUIButton) {
        if (sender.alreadyRead == false){
            notificationReadDict[sender.notificationCompleteData.messageID ?? ""] = true;
            saveNotifPref();
            unreadNotifCount = numOfUnreadInArray(arr: filterThroughSelectedNotifcations());
            UIApplication.shared.applicationIconBadgeNumber = unreadNotifCount;
        }
        if (articleDictionary[sender.notificationCompleteData.notificationArticleID ?? ""] != nil){
            /*articleContentInSegue = articleDictionary[sender.notificationCompleteData.notificationArticleID ?? ""];
            performSegue(withIdentifier: "notificationToArticle", sender: nil);*/
            guard let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(identifier: "articlePageController") as? articlePageViewController else{
                return;
            };
            vc.transitioningDelegate = self;
            vc.interactor = interactor;
            vc.articleContent = articleDictionary[sender.notificationCompleteData.notificationArticleID ?? ""];
            transition(to: vc);
        }
        loadScrollView();
    }
    
    func getLocalNotifications(){
        setUpConnection();
        if (internetConnected){
            totalNotificationList = [notificationData]();
            ref.child("notifications").observeSingleEvent(of: .value) { (snapshot) in
                let enumerator = snapshot.children;
                while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                    let enumerator = article.children;
                    var singleNotification = notificationData();
                    singleNotification.messageID = article.key;
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
                    //filterTotalNotificationArticles();
                  //  print("found notifcaiton article on firebase");
                    self.loadScrollView();
                    self.refreshControl.endRefreshing();
                };
            }
            
        }
        else{
            let infoPopup = UIAlertController(title: "No internet connection detected", message: "No notifications were loaded", preferredStyle: UIAlertController.Style.alert);
            infoPopup.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
                self.refreshControl.endRefreshing();
            }));
            present(infoPopup, animated: true, completion: nil);
        }
    }
    
    func setUpArticleDictionary(){
        setUpConnection();
        if (internetConnected){
            for i in 0...2{
                var s: String; // path inside homepage
                switch i {
                case 0: // general info
                    s = "General_Info";
                    break;
                case 1: // district
                    s = "District";
                    break;
                case 2: // asb
                    s = "ASB";
                    break;
                default:
                    s = "";
                    break;
                }
                
                ref.child("homepage").child(s).observeSingleEvent(of: .value) { (snapshot) in
                    let enumerator = snapshot.children;
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        let enumerator = article.children;
                        var singleArticle = articleData();
                        singleArticle.articleID = article.key;
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                            if (articleContent.key == "articleAuthor"){
                                singleArticle.articleAuthor = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleBody"){
                                singleArticle.articleBody = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleUnixEpoch"){
                                singleArticle.articleUnixEpoch = articleContent.value as? Int64;
                            }
                            else if (articleContent.key == "articleImages"){
                                
                                var tempImage = [String]();
                                let imageIt = articleContent.children;
                                while let image = imageIt.nextObject() as? DataSnapshot{
                                    tempImage.append(image.value as! String);
                                }
                                //print(tempImage)
                                singleArticle.articleImages = tempImage;
                            }
                            else if (articleContent.key == "articleVideoIDs"){
                                var tempArr = [String]();
                                let idIt = articleContent.children;
                                while let id = idIt.nextObject() as? DataSnapshot{
                                    tempArr.append(id.value as! String);
                                }
                                singleArticle.articleVideoIDs = tempArr;
                            }
                            else if (articleContent.key == "articleTitle"){
                                singleArticle.articleTitle = articleContent.value as? String;
                            }
                            else if (articleContent.key == "isFeatured"){
                                singleArticle.isFeatured = (articleContent.value as? Int == 0 ? false : true);
                            }
                            else if (articleContent.key == "hasHTML"){
                                singleArticle.hasHTML = (articleContent.value as? Int == 0 ? false : true);
                            }
                        }
                        singleArticle.articleCatagory = i == 0 ? "General Info" : s;
                        self.articleDictionary[singleArticle.articleID ?? ""] = singleArticle;
                    }
                };
            }
            
            for i in 0...4{
                var s: String; // path inside homepage
                switch i {
                case 0: // seniors
                    s = "Academics";
                    break;
                case 1: // colleges
                    s = "Athletics";
                    break;
                case 2: // events
                    s = "Clubs";
                    break;
                case 3: // athletics
                    s = "Colleges";
                    break;
                case 4: // reference
                    s = "Reference";
                    break;
                default:
                    s = "";
                    break;
                }
                ref.child("bulletin").child(s).observeSingleEvent(of: .value) { (snapshot) in
                    let enumerator = snapshot.children;
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        let enumerator = article.children;
                        var singleArticle = bulletinArticleData();
                        
                        singleArticle.articleID = article.key;
                        
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                            
                            
                            if (articleContent.key == "articleBody"){
                                singleArticle.articleBody = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleUnixEpoch"){
                                singleArticle.articleUnixEpoch = articleContent.value as? Int64;
                            }
                                
                            else if (articleContent.key == "articleTitle"){
                                singleArticle.articleTitle = articleContent.value as? String;
                            }
                            else if (articleContent.key == "hasHTML"){
                                singleArticle.hasHTML = (articleContent.value as? Int == 0 ? false : true);
                            }
                            
                        }
                        singleArticle.articleCatagory = s;
                        singleArticle.articleType = i;
                        self.articleDictionary[singleArticle.articleID ?? ""] = bulletinDataToarticleData(data: singleArticle);
                    }
                };
            }
        }
    }
    
    
   /* override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "notificationToArticle"){
            let vc = segue.destination as! articlePageViewController;
            vc.articleContent = articleContentInSegue;
        }
    }*/
    

    var notificationFrame = CGRect(x: 0, y: 0, width: 0, height: 0);
    
    var horizontalPadding = CGFloat(10);
    var verticalPadding = CGFloat(10);
    
    let timeStampLength = CGFloat(100);
    
    var refreshControl = UIRefreshControl();
    
    func typeIDToString(id: Int) -> String{
        if (id == 0){
            return "Mandatory";
        }
        else if (id == 1){
            return "General";
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
    
    func notificationSort(a: notificationData, b: notificationData)->Bool{
        let currTime = Int64(NSDate().timeIntervalSince1970);
        if (a.notificationUnixEpoch ?? INT64_MAX > currTime && b.notificationUnixEpoch ?? INT64_MAX > currTime){
            return (a.notificationUnixEpoch ?? INT64_MAX) < (b.notificationUnixEpoch ?? INT64_MAX);
        }
        else{
            return (a.notificationUnixEpoch ?? INT64_MAX) > (b.notificationUnixEpoch ?? INT64_MAX);
        }
    }
    
    
    func loadScrollView(){
        
        // remove prev subviews
        for subview in notificationScrollView.subviews{
            if (subview.tag == 1){
                subview.removeFromSuperview();
            }
        }
        
        var currNotifList = filterThroughSelectedNotifcations();
        currNotifList.sort(by: notificationSort);
        
        if (currNotifList.count > 0){
            
            notificationScrollView.isHidden = false;
            noNotificationLabel.isHidden = true;
            
            notificationFrame.size.width = UIScreen.main.bounds.size.width - (2 * horizontalPadding);
            notificationFrame.size.height = 130;
            
            var yPos = verticalPadding;
            // add notification label at start
            for currNotif in currNotifList{
                notificationFrame.origin.x = horizontalPadding;
                notificationFrame.origin.y = yPos;
                
                let notificationButton = notificationUIButton(frame: notificationFrame);
                let currArticleRead = notificationReadDict[currNotif.messageID ?? ""] == true ? true : false;
                
                let chevronWidth = CGFloat(22);
                
                let notificationCatagoryLabelHeight = CGFloat(20);
                let notificationCatagoryLabelFrame = CGRect(x: 10, y: 12, width: 65, height: notificationCatagoryLabelHeight);
                let notificationCatagoryLabel = UILabel(frame: notificationCatagoryLabelFrame);
                notificationCatagoryLabel.text = typeIDToString(id: currNotif.notificationCatagory ?? 0);
                notificationCatagoryLabel.textAlignment = .center;
                notificationCatagoryLabel.textColor = UIColor.white;
                notificationCatagoryLabel.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                notificationCatagoryLabel.backgroundColor = currArticleRead ? makeColor(r: 144, g: 75, b: 75) : mainThemeColor;
                notificationCatagoryLabel.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                //SFProText-Bold, SFProDisplay-Regular, SFProDisplay-Semibold, SFProDisplay-Black
                
                if (currArticleRead == false){
                    let readLabelFrame = CGRect(x: 15 + notificationCatagoryLabelFrame.size.width, y: 12, width: 40, height: notificationCatagoryLabelHeight);
                    let readLabel = UILabel(frame: readLabelFrame);
                    readLabel.text = "New";
                    readLabel.backgroundColor = UIColor.systemYellow;
                    readLabel.textAlignment = .center;
                    readLabel.textColor = UIColor.white;
                    readLabel.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                    readLabel.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                    notificationButton.addSubview(readLabel);
                }
                
                let notificationTitleFrame = CGRect(x: 10, y: notificationCatagoryLabelHeight + 15, width: notificationFrame.size.width - chevronWidth - timeStampLength, height: 30);
                let notificationTitle = UILabel(frame: notificationTitleFrame);
                notificationTitle.text = currNotif.notificationTitle;
                notificationTitle.font = currArticleRead ? UIFont(name: "SFProDisplay-Semibold",size: 18) : UIFont(name:"SFProText-Bold",size: 18);
                notificationTitle.textColor = currArticleRead ? UIColor.gray : UIColor.black;
                notificationTitle.adjustsFontSizeToFitWidth = true;
                notificationTitle.minimumScaleFactor = 0.2;
                
                let bodyVerticalPadding = CGFloat(10);
                
                let notificationBodyWidth = CGFloat(notificationFrame.size.width  - chevronWidth - 27);
                let notificationBodyFrame = CGRect(x: 10, y: notificationTitleFrame.size.height + 10 + notificationCatagoryLabelFrame.size.height + bodyVerticalPadding, width: notificationBodyWidth, height: currNotif.notificationBody?.getHeight(withConstrainedWidth: notificationBodyWidth, font: UIFont(name:"SFProDisplay-Regular",size: 14)!) ?? 0);
                let notificationBodyText = UILabel(frame: notificationBodyFrame);
                notificationBodyText.text = currNotif.notificationBody;
                notificationBodyText.numberOfLines = 0;
                notificationBodyText.font = UIFont(name:"SFProDisplay-Regular",size: 14);
                notificationBodyText.textColor = currArticleRead ? UIColor.gray : UIColor.black;
                // notificationBodyText.backgroundColor = UIColor.lightGray;
                
                let timeStampFrame = CGRect(x: notificationFrame.size.width - chevronWidth - timeStampLength + 10, y: 5, width: timeStampLength, height: 30);
                let timeStamp = UILabel(frame: timeStampFrame);
                timeStamp.text = epochClass.epochToString(epoch: currNotif.notificationUnixEpoch ?? -1);
                timeStamp.font = UIFont(name:"SFProDisplay-Regular",size: 12);
                timeStamp.textAlignment = .right;
                timeStamp.textColor = UIColor.darkGray;
                
                notificationButton.backgroundColor = currArticleRead ? makeColor(r: 250, g: 250, b: 250) : UIColor.white;
                
                notificationButton.layer.shadowColor = UIColor.black.cgColor;
                notificationButton.layer.shadowOpacity = 0.2;
                notificationButton.layer.shadowRadius = 5;
                notificationButton.layer.shadowOffset = CGSize(width: 0 , height:3);
                
                notificationButton.notificationCompleteData = currNotif;
               // notificationButton.articleIndex = nIndex;
            
                notificationButton.addSubview(notificationCatagoryLabel);
                notificationButton.addSubview(notificationTitle);
                notificationButton.addSubview(notificationBodyText);
                notificationButton.addSubview(timeStamp);
                
                notificationButton.frame.size.height = notificationBodyText.frame.maxY + bodyVerticalPadding + 10;
                
                if (currNotif.notificationArticleID != nil && articleDictionary[currNotif.notificationArticleID ?? ""] != nil){
                    //   print("id - \(currNotif.notificationArticleID) & title = \(articleDictionary[currNotif.notificationArticleID ?? ""]?.articleTitle)")
                    let chevronFrame = CGRect(x: notificationButton.frame.size.width-chevronWidth-15, y: (notificationButton.frame.size.height/2)-(chevronWidth/2), width: chevronWidth-5, height: chevronWidth);
                    let chevronImage = UIImageView(frame: chevronFrame);
                    chevronImage.image = UIImage(systemName: "chevron.right");
                    chevronImage.tintColor = UIColor.gray;
                    notificationButton.addSubview(chevronImage);
                }
                
                yPos += notificationButton.frame.size.height + verticalPadding;
                
                notificationButton.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
                notificationButton.tag = 1;
                notificationScrollView.addSubview(notificationButton);
            }
            notificationScrollView.contentSize = CGSize(width: notificationFrame.size.width, height: yPos + verticalPadding);
            notificationScrollView.delegate = self;
        }
        else{
            notificationScrollView.isHidden = true;
            noNotificationLabel.isHidden = false;
        }
        
        //  notificationScrollView.addSubview(refreshControl)
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
        //notificationList = [[notificationData]](repeating: [notificationData](), count: 2);
        
        refreshControl.addTarget(self, action: #selector(refreshNotifications), for: UIControl.Event.valueChanged);
        notificationScrollView.addSubview(refreshControl);
        notificationScrollView.isScrollEnabled = true;
        notificationScrollView.alwaysBounceVertical = true;
        refreshControl.beginRefreshing();
        setUpArticleDictionary();
        loadNotifPref();
        getLocalNotifications();
        
    }
    
}
