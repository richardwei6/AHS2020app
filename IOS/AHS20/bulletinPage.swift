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

    @IBOutlet weak var filterScrollView: UIScrollView!
    @IBOutlet weak var bulletinScrollView: UIScrollView!
    
    @IBOutlet weak var monthLabel: UILabel!
    
    @IBOutlet weak var notificationBellButton: UIButton!
    
    // padding variables
    let iconHorizontalPadding = CGFloat(20);
    let articleHorizontalPadding = CGFloat(10);
    let articleVerticalPadding = CGFloat(10);
    let articleVerticalSize = CGFloat(120);
    
    
    let filterSize = 6;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    var filterIconSize = CGFloat(85);
    var filterIconImageSize = CGFloat(40);
    
    
    var bulletinSize = 10;
    var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
    
    
    let filterIconPicturePath = ["Group 33.png","Path 44.png","Group 50.png","Path 43.png","Path 45.png"];
    let filterIconName = ["Seniors", "Colleges", "Events", "Athletics", "Reference", "Others"];
    
    var selectedFilters: [Bool] = [false, false, false, false, false, false]; // selected types in this order - seniors, colleges, events, athletics, reference, and others
    
    
    @objc func openNotifcations(sender: UIButton){
        print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }
    
    @objc func openArticle(sender: CustomUIButton){
        print("Button pressed");
        performSegue(withIdentifier: "articleSegue", sender: nil);
    }
    
    @objc func addFilter(sender: CustomUIButton){
        if (sender.isSelected){ // selected to unselected
            sender.backgroundColor = UIColor.white;
        }else{ // unselected to selected
            sender.backgroundColor = makeColor(r: 228, g: 182, b: 189);
        }
        sender.isSelected = !sender.isSelected;
        print(sender.isSelected);
        selectedFilters[sender.articleIndex] = sender.isSelected;
        generateBulletin();
    }
    
    
    func generateBulletin(){ // TODO: implement filter ---------
        // set up bulletin
        bulletinFrame.size.height = articleVerticalSize;
        bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
        
        for aIndex in 0..<bulletinSize{
            bulletinFrame.origin.x = articleHorizontalPadding;
            bulletinFrame.origin.y = ((bulletinFrame.size.height+articleVerticalPadding)*CGFloat(aIndex));
            let articleButton = UIView(frame: bulletinFrame);
            articleButton.backgroundColor = makeColor(r: 247, g: 238, b: 239);
            
            // content inside button
            let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding) - 20, height: bulletinFrame.size.height - 10);
            let mainView = CustomUIButton(frame: mainViewFrame);
            
            let articleIconFrame = CGRect(x: 0, y: 0, width: 40, height: 40);
            let articleIcon = UIImageView(frame: articleIconFrame);
            articleIcon.image = UIImage(named: "Group 33.png");
            
            let articleTitleFrame = CGRect(x: 50, y : 5, width: 100, height: 60);
            let articleTitleText = UILabel(frame: articleTitleFrame);
            articleTitleText.text = "Title"; // insert title text here
            articleTitleText.font =  UIFont(name: "SFProDisplay-Regular",size: 30);
            articleTitleText.numberOfLines = 1;
            articleTitleText.adjustsFontSizeToFitWidth = true;
            articleTitleText.sizeToFit();

            
            
            let articleBodyFrame = CGRect(x: 0, y: 40, width: mainViewFrame.size.width, height: mainViewFrame.size.height - 50);
            let articleBodyText = UILabel(frame: articleBodyFrame);
            articleBodyText.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nec blandit erat, a pellentesque urna. Donec eget tristique elit, non mattis mauris. Duis eget feugiat nisi, eget ornare velit. Maecenas a malesuada orci. Sed suscipit augue vitae turpis blandit, sit amet condimentum nulla blandit. Integer malesuada sed dolor vel ultrices. Aenean eget ligula pulvinar leo hendrerit ornare eget a augue. Aenean id hendrerit erat, in sodales massa. Etiam eu finibus justo. Morbi nunc eros, aliquam non erat eu, tincidunt vulputate mauris. Integer non nibh a nisi vestibulum condimentum. Etiam et sapien lacus. Donec sollicitudin, turpis quis aliquam hendrerit, sem arcu consectetur erat, sed bibendum sapien sapien vel ante. Duis eget mi feugiat, aliquet diam id, vehicula tellus. Maecenas et rutrum metus, in sodales nulla." // insert body text here
            articleBodyText.numberOfLines = 4;
            articleBodyText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
            
            
            mainView.addSubview(articleIcon);
            mainView.addSubview(articleTitleText);
            //mainView.addSubview(dateText);
            mainView.addSubview(articleBodyText);
            
            let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 95, y : 0, width: 100, height: 25);
            let dateText = UILabel(frame: dateTextFrame);
            dateText.text = "00/00/0000"; // insert date here
            dateText.textColor = makeColor(r: 189, g: 151, b: 104);
            dateText.textAlignment = .right;

            
            articleButton.addSubview(dateText);
            articleButton.addSubview(mainView);
            
            articleButton.layer.cornerRadius = 10;
            
            mainView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
            self.bulletinScrollView.addSubview(articleButton);
        }
        bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width-(2*articleHorizontalPadding), height: articleHorizontalPadding+(bulletinFrame.size.height+articleHorizontalPadding)*CGFloat(bulletinSize));
        bulletinScrollView.delegate = self;
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
       
        
        let textMargin = CGFloat(20);
        filterIconSize = filterScrollView.frame.size.height-textMargin;
        
        monthLabel.text = getTitleDateAndMonth();
        monthLabel.adjustsFontSizeToFitWidth = true;
        monthLabel.minimumScaleFactor = 0.8;
        
        notificationBellButton.addTarget(self, action: #selector(self.openNotifcations), for: .touchUpInside);
        
        // set up both scrollviews here
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterScrollView.frame.size.height;
        filterFrame.size.width = filterScrollView.frame.size.height; //
        
        for buttonIndex in 0..<filterSize{
            filterFrame.origin.x = articleHorizontalPadding+((filterIconSize+iconHorizontalPadding) * CGFloat(buttonIndex));
            /*let buttonView = UIButton(frame: filterFrame);
            //buttonView.setImage("", for: .normal);
            buttonView.layer.borderColor = makeColor(r: 127, g: 47, b: 60).cgColor;
            buttonView.layer.borderWidth = 3;
            buttonView.layer.cornerRadius = 45;
            if (buttonIndex != 0){
                buttonView.setImage(UIImage(named: filterIconName[buttonIndex-1]), for: .normal);
            }else{
                let yearTextFrame = CGRect(x: 0, y: 5, width: filterFrame.size.width, height: filterFrame.size.height);
                let yearText = UILabel(frame: yearTextFrame);
                yearText.text = "20\n21";
                yearText.setLineSpacing(lineHeightMultiple: 0.8);
                yearText.textColor = makeColor(r: 127, g: 47, b: 60);
                yearText.numberOfLines = 2;
                yearText.textAlignment = .center;
                yearText.font = UIFont(name: "HarlowSolid", size: 30);
                buttonView.addSubview(yearText);
            }
            */
            
            let mainView = UIView(frame: filterFrame);
            
            let iconViewFrame = CGRect(x:0, y:0, width: filterIconSize, height: filterIconSize);
            let iconView = CustomUIButton(frame: iconViewFrame);
            iconView.layer.cornerRadius = filterIconSize/2;
            iconView.layer.borderColor = makeColor(r: 127, g: 47, b: 60).cgColor;
            iconView.layer.borderWidth = 3;
            iconView.articleIndex = buttonIndex;
            
            if (buttonIndex != 0){
                let iconImageFrame = CGRect(x:(iconViewFrame.size.width/2) - (filterIconImageSize/2), y: (iconViewFrame.size.height/2) - (filterIconImageSize/2), width: filterIconImageSize, height: filterIconImageSize );
                let iconImageView = UIImageView(frame: iconImageFrame);
                iconImageView.image = UIImage(named: filterIconPicturePath[buttonIndex-1]);
                iconImageView.contentMode = .scaleAspectFit;
                
                iconView.addSubview(iconImageView);
                
            }else{
                let yearTextFrame = CGRect(x: 0, y: 5, width: iconViewFrame.size.width, height: iconViewFrame.size.height);
                let yearText = UILabel(frame: yearTextFrame);
                yearText.text = "20\n21";
                yearText.setLineSpacing(lineHeightMultiple: 0.8);
                yearText.textColor = makeColor(r: 127, g: 47, b: 60);
                yearText.numberOfLines = 2;
                yearText.textAlignment = .center;
                yearText.font = UIFont(name: "HarlowSolid", size: 30);
                iconView.addSubview(yearText);
            }
        
            
            iconView.addTarget(self, action: #selector(self.addFilter), for: .touchUpInside);
            
            
            let iconTextFrame = CGRect(x:0, y: filterIconSize, width: filterIconSize, height: textMargin);
            let iconText = UILabel(frame: iconTextFrame);
            iconText.text = filterIconName[buttonIndex];
            iconText.numberOfLines = 1;
            iconText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
            iconText.textAlignment = .center;
            
            
            mainView.addSubview(iconView);
            mainView.addSubview(iconText);
            
            
            self.filterScrollView.addSubview(mainView);
        }
        filterScrollView.contentSize = CGSize(width: (filterIconSize+iconHorizontalPadding) * CGFloat(filterSize)+iconHorizontalPadding, height: filterScrollView.frame.size.height);
        filterScrollView.delegate = self;
        
        
       // set up bulletin for the first time before any filters
        generateBulletin();
        
        
    }

}
