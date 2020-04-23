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
    
    // padding variables
    let iconHorizontalPadding = CGFloat(10);
    let articleHorizontalPadding = CGFloat(10);
    let articleVerticalPadding = CGFloat(10);
    let articleVerticalSize = CGFloat(120);
    
    
    let filterSize = 6;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    var filterIconSize = CGFloat(85);
    
    
    var bulletinSize = 10;
    var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
    
    
    let filterIconName = ["Group 33.png","Path 44.png","Group 34.png","Path 43.png","Path 45.png"];
    
    
    @objc func openArticle(){
        print("Button pressed");
        performSegue(withIdentifier: "articleSegue", sender: nil);
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
       
        filterIconSize = filterScrollView.frame.size.height;
        
        let month = Calendar.current.component(.month, from: Date());
        monthLabel.text = "Month " + (month < 10 ? "0":"") + String(month);
        
        // set up both scrollviews here
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterIconSize;
        filterFrame.size.width = filterIconSize; //
        
        for buttonIndex in 0..<filterSize{
            filterFrame.origin.x = articleHorizontalPadding+((filterIconSize+iconHorizontalPadding) * CGFloat(buttonIndex));
            let buttonView = UIButton(frame: filterFrame);
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
            
            
            self.filterScrollView.addSubview(buttonView);
        }
        filterScrollView.contentSize = CGSize(width: (filterIconSize+iconHorizontalPadding) * CGFloat(filterSize)+iconHorizontalPadding, height: filterScrollView.frame.size.height);
        filterScrollView.delegate = self;
        
        
        // set up bulletin
        bulletinFrame.size.height = articleVerticalSize;
        bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
        
        for aIndex in 0..<bulletinSize{
            bulletinFrame.origin.x = articleHorizontalPadding;
            bulletinFrame.origin.y = ((bulletinFrame.size.height+articleVerticalPadding)*CGFloat(aIndex));
            let articleButton = UIView(frame: bulletinFrame);
            articleButton.backgroundColor = makeColor(r: 204, g: 174, b: 148);
            
            // content inside button
            let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding) - 20, height: bulletinFrame.size.height - 10);
            let mainView = UIButton(frame : mainViewFrame);
            
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
            
            let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 100, y : 0, width: 100, height: 25);
            let dateText = UILabel(frame: dateTextFrame);
            dateText.text = "00/00/0000"; // insert date here
            dateText.textColor = UIColor.white;
            dateText.textAlignment = .right;

            
            articleButton.addSubview(dateText);
            articleButton.addSubview(mainView);
            
            articleButton.layer.cornerRadius = 10;
            
            mainView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside)
            self.bulletinScrollView.addSubview(articleButton);
        }
        bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width-(2*articleHorizontalPadding), height: articleHorizontalPadding+(bulletinFrame.size.height+articleHorizontalPadding)*CGFloat(bulletinSize));
        bulletinScrollView.delegate = self;
        
        
        
    }

}
