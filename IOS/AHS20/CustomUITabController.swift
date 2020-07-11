//
//  CustomUITabController.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import Firebase


class CustomTabBarController: UIViewController {

    @IBOutlet weak var contentView: UIView!
    
    @IBOutlet weak var tabBarView: UIView!
    
    @IBOutlet weak var notificationDot: UIImageView!
    @IBOutlet weak var notificationButton: UIButton!
    @IBOutlet var buttons: [UIButton]!
    
    @IBOutlet weak var topBar: UIView!
    @IBOutlet weak var topBarHeightContraint: NSLayoutConstraint!
    
    @IBOutlet weak var topBarPageName: UILabel!
    @IBOutlet weak var homeTopBarContent: UIView!
    @IBOutlet weak var dateLabel: UILabel!
    
    var homeViewController: UIViewController!
    var bulletinViewController: UIViewController!
    var savedViewController: UIViewController!
    var settingsViewController: UIViewController!
    
    
    var viewControllers: [UIViewController]!
    
    var selectedIndex: Int = 0;
    
    let homeTopCornerRadius = CGFloat(15);

    let iconImagePath = ["invertedhome", "invertedbulletin", "invertedbookmark", "invertedsettings"];
    let iconImagePathInv = ["homeInv", "bulletinInv", "BookmarkInv", "GearInv"];
    let selectedColor = makeColor(r: 243, g: 149, b: 143);
    
    var articleContentInSegue: articleData?;
    
    @IBAction func openNotifications(_ sender: UIButton) {
        print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }
    
    @objc func articleSelector(notification: NSNotification){
        articleContentInSegue = notification.userInfo?["articleContent"] as? articleData;
     //   print("pre segue");
      //  print(articleContentInSegue)
        //prepare(for: "articleSegue", sender: articleContentInSegue)
        performSegue(withIdentifier: "articleSegue", sender: nil);
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "articleSegue"){
        //    print("segue");
         //   print(articleContentInSegue)
            let vc = segue.destination as! articlePageViewController;
            vc.articleContent = articleContentInSegue;
        }
    }
   
    func setUpNotifDot(){
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
                    
                    singleNotification.messageID =  article.key as! String;
                    
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
                    loadNotifPref();
                    filterTotalArticles();
                    unreadNotif = (notificationList[1].count > 0);
                    self.notificationDot.isHidden = !unreadNotif;
                };
            }
        }
        else{
            //setUpAllViews();
            print("no network detected - notifications");
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
      
        selectedNotifications = UserDefaults.standard.array(forKey: "selectedNotifications") as? [Bool] ?? [true, false, false, false, false];
        
        setUpConnection();
        print("Connection Established");

        setUpNotifDot();
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.articleSelector), name:NSNotification.Name(rawValue: "article"), object: nil);
        
        
       // getSavedArticles(); // load default saved articles
        savedArticleClass.getSavedArticles();
        
        fontSize = UserDefaults.standard.integer(forKey: "fontSize") != 0 ? UserDefaults.standard.integer(forKey: "fontSize") : 20;
        
        //contentView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        //contentView.topAnchor.constraint(equalTo: view.topAnchor, constant: -1).isActive = true;
        
        //tabBarView.bottomAnchor.constraint(equalTo: self.view.bottomAnchor, constant: 0).isActive = true;
        
        
    
       // tabBarView.frame.size.height = CGFloat(70);
        

        // set up buttons
        for index in 0..<buttons.count{
            let image = UIImage(named: iconImagePath[index]);
            //image = image?.maskWithColor(color: UIColor.white);
            buttons[index].setImage(image, for: .normal);
            buttons[index].tintColor = UIColor.black;
        }
        
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil);
        homeViewController = storyboard.instantiateViewController(withIdentifier: "homeViewController") as! homeClass;
        bulletinViewController = storyboard.instantiateViewController(withIdentifier: "bulletinViewController") as! bulletinClass;
        savedViewController = storyboard.instantiateViewController(withIdentifier: "savedViewController") as! savedClass;
        settingsViewController = storyboard.instantiateViewController(withIdentifier: "settingsViewController") as! settingClass;
        viewControllers = [homeViewController, bulletinViewController, savedViewController, settingsViewController];
        buttons[selectedIndex].setImage(UIImage(named: iconImagePathInv[selectedIndex]), for: .normal);
        //buttons[selectedIndex].tintColor = selectedColor;
       let vc = viewControllers[selectedIndex];
        addChild(vc);
        vc.view.frame = contentView.bounds;
        contentView.addSubview(vc.view);
        vc.didMove(toParent: self);
        topBar.layer.cornerRadius = homeTopCornerRadius;
        topBar.layer.maskedCorners = [.layerMaxXMaxYCorner, .layerMinXMaxYCorner];
        topBar.layer.shadowColor = UIColor.gray.cgColor;
        topBar.layer.shadowOpacity = 0.07;
        topBar.layer.shadowRadius = 5;
        topBar.layer.shadowOffset = CGSize(width: 0 , height:10);
        tabBarView.layer.shadowColor = UIColor.gray.cgColor;
        tabBarView.layer.shadowOpacity = 0.07;
        tabBarView.layer.shadowRadius = 5;
        tabBarView.layer.shadowOffset = CGSize(width: 0, height: -10);
        
        dateLabel.text = getTitleDateAndMonth();
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        // set notification dot
        notificationDot.isHidden = !unreadNotif;
    }
    
    @IBAction func didPressTab(_ sender: UIButton) {
        let prevIndex = selectedIndex;
        selectedIndex = sender.tag;
        if (prevIndex == sender.tag){
            if (sender.tag == 0){
                // add code here
                if let page  = viewControllers[0] as? homeClass{
                    page.mainScrollView.setContentOffset(.zero, animated: true);
                }
                
            }
            if (sender.tag == 2){
                if let page = viewControllers[sender.tag] as? savedClass{
                    page.mainScrollView.setContentOffset(.zero, animated: true);
                }
            }
            if (sender.tag == 1){
                if let page = viewControllers[sender.tag] as? bulletinClass{
                    page.bulletinScrollView.setContentOffset(.zero, animated: true);
                }
            }
        }
        else{
        // remove prev view controller
        //buttons[prevIndex].isSelected = false;
           // buttons[prevIndex].setImage(UIImage(named: iconImagePath[prevIndex]), for: .normal);
        //buttons[prevIndex].tintColor = UIColor.white;
        
            let prevVC = viewControllers[prevIndex];
            prevVC.willMove(toParent: nil);
            prevVC.view.removeFromSuperview();
            prevVC.removeFromParent();
        
        // add current view controller
       // sender.isSelected = true;
       // sender.tintColor = selectedColor;
            //sender.setImage(UIImage(named: iconImagePathInv[sender.tag]), for: .normal);
            let vc = viewControllers[selectedIndex];
            addChild(vc);
            vc.view.frame = contentView.bounds;
            contentView.addSubview(vc.view);
            vc.didMove(toParent: self);
            
            
            if (sender.tag == 0){
                print("home")
                topBar.layer.cornerRadius = homeTopCornerRadius;
                topBar.layer.maskedCorners = [.layerMaxXMaxYCorner, .layerMinXMaxYCorner];
                topBarHeightContraint.constant = 62;
                homeTopBarContent.isHidden = false;
                topBarPageName.isHidden = true;
            }
            else{
                topBar.layer.cornerRadius = 0;
                topBar.layer.maskedCorners = [.layerMaxXMaxYCorner, .layerMinXMaxYCorner];
                topBarHeightContraint.constant = 60;
                homeTopBarContent.isHidden = true;
                topBarPageName.isHidden = false;
                if (sender.tag == 1){
                    topBarPageName.text = "Student Bulletin";
                }
                else if (sender.tag == 2){
                    topBarPageName.text = "Saved Articles";
                }
                else if (sender.tag == 3){
                    topBarPageName.text = "Settings";
                }
            }
            
        }
            
    }
    
    
}


