//
//  bulletinPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class bulletinClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    
    var comingUpSize = 10;
    var comingUpFrame = CGRect(x:0,y:0,width:0,height:0);
    
    
    var buttonTextEdgeInset = UIEdgeInsets(top: 0, left: 50, bottom: 0, right: 0);
    
    override func viewDidLoad() {
        super.viewDidLoad();
        /*
        bulletinLabel.setRoundedEdge(corners: [.bottomLeft,.bottomRight], radius: 30);
        comingUpLabel.setRoundedEdge(corners: [.bottomLeft,.bottomRight], radius: 30);
        
        // set up correct spacing between text on button and image
        seniorButton.contentEdgeInsets = buttonTextEdgeInset;
        seniorButton.contentHorizontalAlignment = .left;
        collegeButton.contentEdgeInsets = buttonTextEdgeInset;
        collegeButton.contentHorizontalAlignment = .left;
        athleticsButton.contentEdgeInsets = buttonTextEdgeInset;
        athleticsButton.contentHorizontalAlignment = .left;
        eventsButton.contentEdgeInsets = UIEdgeInsets(top: 0, left: 55, bottom: 0, right: 0);
        eventsButton.contentHorizontalAlignment = .left;
        referenceButton.contentEdgeInsets = UIEdgeInsets(top: 0, left: 45, bottom: 0, right: 0);
        referenceButton.contentHorizontalAlignment = .left;
        otherButton.contentEdgeInsets = buttonTextEdgeInset;
        otherButton.contentHorizontalAlignment = .left;
        seniorButton.layer.cornerRadius = 14;
        collegeButton.layer.cornerRadius = 14;
        athleticsButton.layer.cornerRadius = 14;
        eventsButton.layer.cornerRadius = 14;
        referenceButton.layer.cornerRadius = 14;
        otherButton.layer.cornerRadius = 14;
        
        // set up scroll view
        let spacing = 10;
        comingUpFrame.size.height = 120;
        comingUpFrame.size.width = UIScreen.main.bounds.size.width - 44;
        for aIndex in 0..<comingUpSize{
            comingUpFrame.origin.y = (comingUpFrame.size.height*CGFloat(aIndex)) + CGFloat(spacing*aIndex);
            
            let contentButton = UIButton(frame: comingUpFrame);
            contentButton.backgroundColor = UIColor.white;
            
            // content inside contentView ---
            let imageFrame = CGRect(x:10, y:10, width: 30, height: 30);
            let imageView = UIImageView(frame: imageFrame);
            imageView.image = UIImage(named: "college-selected.png");
            
            
            let titleFrame = CGRect(x:55, y:15, width: 100, height: 30);
            let titleText = UILabel(frame: titleFrame);
            titleText.text = "Title";
            titleText.font = UIFont(name: "DINCondensed-Bold", size: 30);
            
            
            let dateFrame = CGRect(x:comingUpFrame.size.width-110, y: 10, width: 100, height: 20);
            let dateText = UILabel(frame: dateFrame);
            dateText.text = "00/00/0000";
            
            
            let contentFrame = CGRect(x:10, y:45, width: comingUpFrame.size.width-20, height: comingUpFrame.size.height - 45);
            let contentText = UILabel(frame: contentFrame);
            contentText.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eleifend erat ultricies bibendum bibendum. Nulla imperdiet dui a auctor tristique. Aliquam lobortis tellus vel mauris congue lobortis. Etiam rutrum ultrices convallis. Sed nulla felis, tincidunt vel diam eget, consectetur lobortis magna. Praesent quis lacus mi. Integer varius, nisi at ullamcorper sagittis, dolor elit ornare lorem, ac commodo enim quam convallis nulla. Cras ultricies vestibulum eleifend. Sed at malesuada dolor, et consequat quam. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Interdum et malesuada fames ac ante ipsum primis in faucibus. In consectetur augue consequat ex ornare, non tempor ipsum lacinia. Fusce auctor, felis id consectetur consequat, mauris purus luctus erat, in mattis odio augue in magna. Pellentesque ac congue justo.";
            contentText.font = UIFont(name: contentText.font.fontName, size: 20);
            contentText.numberOfLines = 3;

            
            // add subviews to main button
            contentButton.addSubview(imageView);
            contentButton.addSubview(titleText);
            contentButton.addSubview(dateText);
            contentButton.addSubview(contentText);
            
            contentButton.layer.cornerRadius = 20;
            self.comingUpScrollView.addSubview(contentButton);
        }
        comingUpScrollView.contentSize = CGSize(width: comingUpFrame.size.width, height: comingUpFrame.size.height*CGFloat(comingUpSize) + CGFloat(spacing*comingUpSize));
        comingUpScrollView.delegate = self;
        */
    }

}
