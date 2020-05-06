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

class notificationsClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {


    @IBOutlet weak var notificationScrollView: UIScrollView!
    
    @objc func openArticle(_ sender: notificationUIButton) {
        if (sender.unreadBool == true){
            unreadNotificationSize-=1;
            readNotificationSize+=1;
            sender.unreadBool = false;
        }
        performSegue(withIdentifier: "notificationToArticle", sender: nil);
        loadScrollView();
    }
    
    var unreadNotificationSize = 2;
    var readNotificationSize = 10;
    
    var notificationFrame = CGRect(x: 0, y: 0, width: 0, height: 0);
    
    var horizontalPadding = CGFloat(10);
    var verticalPadding = CGFloat(10);

    
    func loadScrollView(){
        // remove prev subviews
        for subview in notificationScrollView.subviews{
            subview.removeFromSuperview();
        }
        
        notificationFrame.size.width = UIScreen.main.bounds.size.width - (2 * horizontalPadding);
        notificationFrame.size.height = 110;
        // add notification label at start
        for nIndex in 0..<unreadNotificationSize+readNotificationSize{
            notificationFrame.origin.x = horizontalPadding;
            notificationFrame.origin.y = verticalPadding+((notificationFrame.size.height + verticalPadding)*CGFloat(nIndex));
            
            let notificationButton = notificationUIButton(frame: notificationFrame);
            notificationButton.backgroundColor = makeColor(r: 244, g: 244, b: 245);
            
            
            let readIndicatorWidth = CGFloat(20);
            let readIndicatorFrame = CGRect(x: 0, y: 0, width: readIndicatorWidth, height: notificationFrame.size.height);
            let readIndicator = UIView(frame: readIndicatorFrame);
            if (nIndex+1<=unreadNotificationSize){
                readIndicator.backgroundColor = makeColor(r: 230, g: 205, b: 85);
                notificationButton.unreadBool = true;
            }
            else{
                readIndicator.backgroundColor = makeColor(r: 204, g: 204, b: 205);
            }
            
            let chevronWidth = CGFloat(22);
            let chevronFrame = CGRect(x: notificationFrame.size.width-chevronWidth-15, y: (notificationFrame.size.height/2)-(chevronWidth/2), width: chevronWidth, height: chevronWidth);
            let chevronImage = UIImageView(frame: chevronFrame);
            chevronImage.image = UIImage(systemName: "chevron.right");
            chevronImage.tintColor = UIColor.gray;
            
            
            let notificationTitleFrame = CGRect(x: readIndicatorWidth + 5, y: 0, width: 200, height: 50);
            let notificationTitle = UILabel(frame: notificationTitleFrame);
            notificationTitle.text = "Title";
            notificationTitle.font = UIFont(name:"SFProText-Bold",size: 22);
            
            
            let notificationBodyFrame = CGRect(x: readIndicatorWidth + 8, y: notificationTitleFrame.size.height-15, width: notificationFrame.size.width - readIndicatorWidth - chevronWidth - 25, height: notificationFrame.size.height - (notificationTitleFrame.size.height-11));
            let notificationBodyText = UILabel(frame: notificationBodyFrame);
            notificationBodyText.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nec blandit erat, a pellentesque urna. Donec eget tristique elit, non mattis mauris.";
            notificationBodyText.numberOfLines = 3;
            notificationBodyText.font = UIFont(name:"SFProDisplay-Regular",size: 16);
            
            
            let timeStampLength = CGFloat(100);
            let timeStampFrame = CGRect(x: notificationFrame.size.width - chevronWidth - timeStampLength, y: 10, width: timeStampLength, height: 30);
            let timeStamp = UILabel(frame: timeStampFrame);
            timeStamp.text = "00 hours ago";
            timeStamp.font = UIFont(name:"SFProDisplay-Regular",size: 15);
            timeStamp.textAlignment = .left;
            timeStamp.textColor = UIColor.darkGray;
            
            
            notificationButton.addSubview(chevronImage);
            notificationButton.addSubview(readIndicator);
            notificationButton.addSubview(notificationTitle);
            notificationButton.addSubview(notificationBodyText);
            notificationButton.addSubview(timeStamp);
            
            notificationButton.setRoundedEdge(corners: [.bottomLeft, .bottomRight, .topLeft, .topRight], radius: 15);
            notificationButton.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
            notificationScrollView.addSubview(notificationButton);
        }
        notificationScrollView.backgroundColor = makeColor(r: 230, g: 230, b: 230);
        notificationScrollView.contentSize = CGSize(width: notificationFrame.size.width, height: (2*verticalPadding)+((notificationFrame.size.height+verticalPadding)*CGFloat(unreadNotificationSize+readNotificationSize)));
        notificationScrollView.delegate = self;
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        // set iphone x or above color below the safe area
      
        notificationScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        loadScrollView();
        
    }
    
    
    @IBAction func exitPopup(_ sender: UIButton) {
        dismiss(animated: true);
        AudioServicesPlaySystemSound(1519);
    }
    
}
