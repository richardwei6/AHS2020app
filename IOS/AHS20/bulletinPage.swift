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
    let articleVerticalSize = CGFloat(130);
    
    
    let filterSize = 6;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    var filterIconImageSize = CGFloat(40);
    
    
    var bulletinSize = 10;
    var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
    
    
    let filterIconPicturePath = ["Group 51.png","Path 275.png","Group 34.png","Path 276.png","Path 277.png"];
    let filterIconName = ["Seniors", "Colleges", "Events", "Athletics", "Reference", "Others"];
    
    var selectedFilters: [Bool] = [false, false, false, false, false, false]; // selected types in this order - seniors, colleges, events, athletics, reference, and others
    
    
    // icon stuff
    var iconViewFrame: CGRect?;
    
    @objc func openNotifcations(sender: UIButton){
        print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }
    
    @objc func openArticle(sender: CustomUIButton){
        print("Button pressed");
        performSegue(withIdentifier: "articleSegue", sender: nil);
    }
    
    @objc func addFilter(sender: CustomUIButton){
        removeAllSubViews(sender: sender);
        if (sender.isSelected){ // selected to unselected
            sender.backgroundColor = nil;
        }else{ // unselected to selected
            sender.backgroundColor = makeColor(r: 127, g: 47, b: 60);
        }
        sender.isSelected = !sender.isSelected;
        selectedFilters[sender.articleIndex] = sender.isSelected;
        print(sender.isSelected);
        generateIconImage(iconView: sender);
        generateBulletin();
    }
    
    func removeAllSubViews(sender: CustomUIButton){
        for view in sender.subviews{
            view.removeFromSuperview();
        }
    }
    
    
    func generateIconImage(iconView: CustomUIButton){
        if (iconView.articleIndex != 0){
            let imageIconPadding = CGFloat(5);
            let iconImageFrame = CGRect(x:(iconViewFrame!.size.width/2) - (filterIconImageSize/2) + imageIconPadding, y: (iconViewFrame!.size.height/2) - (filterIconImageSize/2)+imageIconPadding, width: filterIconImageSize-(2*imageIconPadding), height: filterIconImageSize-(2*imageIconPadding));
            let iconImageView = UIImageView(frame: iconImageFrame);
            iconImageView.image = UIImage(named: filterIconPicturePath[iconView.articleIndex-1]);
            iconImageView.contentMode = .scaleAspectFit;
            if (iconView.isSelected == true){
                iconImageView.setImageColor(color: UIColor.white);
            }
            else{
                iconImageView.setImageColor(color: mainThemeColor);
            }
            iconView.addSubview(iconImageView);
        }else{
            let yearTextFrame = CGRect(x: 0, y: 5, width: iconViewFrame!.size.width, height: iconViewFrame!.size.height);
            let yearText = UILabel(frame: yearTextFrame);
            yearText.text = "20\n21";
            yearText.setLineSpacing(lineHeightMultiple: 0.7);
            if (iconView.isSelected == true){
                yearText.textColor = UIColor.white;
            }
            else{
                yearText.textColor = mainThemeColor;
            }
            yearText.numberOfLines = 2;
            yearText.textAlignment = .center;
            yearText.font = UIFont(name: "HarlowSolid", size: 20);
            iconView.addSubview(yearText);
        }
    }
    
    func generateBulletin(){ // TODO: implement filter ---------
        // set up bulletin
        bulletinFrame.size.height = articleVerticalSize;
        bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
        
        let imageArticleSize = CGFloat(35);
        
        for aIndex in 0..<bulletinSize{
            bulletinFrame.origin.x = articleHorizontalPadding;
            bulletinFrame.origin.y = articleVerticalPadding+((bulletinFrame.size.height+articleVerticalPadding)*CGFloat(aIndex));
            let articleButton = UIView(frame: bulletinFrame);
            articleButton.backgroundColor = UIColor.white;
            
            // content inside button
            let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding) - 20, height: bulletinFrame.size.height - 10);
            let mainView = CustomUIButton(frame: mainViewFrame);
            
            let articleIconFrame = CGRect(x: 2, y: 7, width: imageArticleSize, height: imageArticleSize);
            let articleIcon = UIImageView(frame: articleIconFrame);
            articleIcon.image = UIImage(named: "Group 51.png");
            articleIcon.setImageColor(color: mainThemeColor);
            
            let articleTitleFrame = CGRect(x: 45, y : 17, width: UIScreen.main.bounds.size.width - articleHorizontalPadding - 100, height: 25);
            let articleTitleText = UILabel(frame: articleTitleFrame);
            articleTitleText.text = "Title"; // insert title text here
            articleTitleText.font =  UIFont(name: "SFProDisplay-Regular",size: 30);
            articleTitleText.numberOfLines = 1;
            //articleTitleText.backgroundColor = UIColor.gray;
            /*articleTitleText.adjustsFontSizeToFitWidth = true;
            articleTitleText.sizeToFit();*/

            
            
            let articleBodyFrame = CGRect(x: 0, y: 44, width: mainViewFrame.size.width, height: mainViewFrame.size.height - 50);
            let articleBodyText = UILabel(frame: articleBodyFrame);
            articleBodyText.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nec blandit erat, a pellentesque urna. Donec eget tristique elit, non mattis mauris. Duis eget feugiat nisi, eget ornare velit. Maecenas a malesuada orci. Sed suscipit augue vitae turpis blandit, sit amet condimentum nulla blandit. Integer malesuada sed dolor vel ultrices. Aenean eget ligula pulvinar leo hendrerit ornare eget a augue. Aenean id hendrerit erat, in sodales massa. Etiam eu finibus justo. Morbi nunc eros, aliquam non erat eu, tincidunt vulputate mauris. Integer non nibh a nisi vestibulum condimentum. Etiam et sapien lacus. Donec sollicitudin, turpis quis aliquam hendrerit, sem arcu consectetur erat, sed bibendum sapien sapien vel ante. Duis eget mi feugiat, aliquet diam id, vehicula tellus. Maecenas et rutrum metus, in sodales nulla." // insert body text here
            articleBodyText.numberOfLines = 4;
            articleBodyText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
            
            
            mainView.addSubview(articleIcon);
            mainView.addSubview(articleTitleText);
            //mainView.addSubview(dateText);
            mainView.addSubview(articleBodyText);
            
            let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 95, y : 5, width: 100, height: 25);
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
        bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width-(2*articleHorizontalPadding), height: 2*articleVerticalPadding+(bulletinFrame.size.height+articleVerticalPadding)*CGFloat(bulletinSize));
        bulletinScrollView.delegate = self;
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
       
        
        
        monthLabel.text = getTitleDateAndMonth();
        monthLabel.adjustsFontSizeToFitWidth = true;
        monthLabel.minimumScaleFactor = 0.8;
        
        notificationBellButton.addTarget(self, action: #selector(self.openNotifcations), for: .touchUpInside);
        
        // set up both scrollviews here
        
        let textMargin = CGFloat(20);
        let topMargin = CGFloat(5);
        let betweenMargin = CGFloat(3);
        let filterIconSize = filterScrollView.frame.size.height-textMargin-topMargin-betweenMargin;
        iconViewFrame = CGRect(x:0, y:topMargin, width: filterIconSize, height: filterIconSize);
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterScrollView.frame.size.height;
        filterFrame.size.width = filterScrollView.frame.size.height; //
        
        for buttonIndex in 0..<filterSize{
            filterFrame.origin.x = 15+((filterIconSize+iconHorizontalPadding) * CGFloat(buttonIndex));

            
            let mainView = UIView(frame: filterFrame);
            
            let iconView = CustomUIButton(frame: iconViewFrame!);
            iconView.layer.cornerRadius = filterIconSize/2;
            iconView.layer.borderColor = makeColor(r: 127, g: 47, b: 60).cgColor;
            iconView.layer.borderWidth = 3;
            iconView.articleIndex = buttonIndex;
            
            
            generateIconImage(iconView: iconView);
            
            
            let iconTextFrame = CGRect(x:0, y: filterIconSize+topMargin+betweenMargin, width: filterIconSize, height: textMargin);
            let iconText = UILabel(frame: iconTextFrame);
            iconText.text = filterIconName[buttonIndex];
            iconText.numberOfLines = 1;
            iconText.font = UIFont(name: "SFProDisplay-Regular", size: 13);
            iconText.textAlignment = .center;
            
            
            iconView.addTarget(self, action: #selector(self.addFilter), for: .touchUpInside);
            
            mainView.addSubview(iconView);
            mainView.addSubview(iconText);
            
            
            self.filterScrollView.addSubview(mainView);
        }
        filterScrollView.contentSize = CGSize(width: (filterIconSize+iconHorizontalPadding) * CGFloat(filterSize)+15, height: filterScrollView.frame.size.height);
        filterScrollView.delegate = self;
        
        
       // set up bulletin for the first time before any filters
        generateBulletin();
        
        
    }

}
