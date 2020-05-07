//
//  savedPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class savedClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    @IBOutlet weak var mainScrollView: UIScrollView!
    
    @IBOutlet weak var monthLabel: UILabel!
    
    @IBOutlet weak var notificationBellButton: UIButton!
    
    @objc func openNotifcations(sender: UIButton){
        print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }
    
    @objc func openArticle(sender: CustomUIButton){
       // print("Button pressed");
        performSegue(withIdentifier: "articleSegue", sender: nil);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        
        monthLabel.text = getTitleDateAndMonth();
        monthLabel.adjustsFontSizeToFitWidth = true;
        monthLabel.minimumScaleFactor = 0.8;
        
        notificationBellButton.addTarget(self, action: #selector(self.openNotifcations), for: .touchUpInside);
        
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        getSavedArticles();
        
//        print("got saved");
//        print(savedArticles.count);
        
        for view in mainScrollView.subviews{
            view.removeFromSuperview();
        }
                
            
        if (savedArticles.count != 0){
            let articleHeight = CGFloat(130);
            let articleHorizontalPadding = CGFloat(11);
            let articleVerticalPadding = CGFloat(10);
            
            mainScrollView.isHidden = false;
            
            mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
            
            var articleFrame = CGRect(x:0, y: 0, width: 0, height: 0);
            articleFrame.size.height = articleHeight;
            articleFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
            for aIndex in 0..<savedArticles.count{
                articleFrame.origin.x = articleHorizontalPadding;
                articleFrame.origin.y = articleVerticalPadding+(articleFrame.size.height+articleVerticalPadding)*CGFloat(aIndex);
                
                let articleButton = UIButton(frame: articleFrame);
                //articleButton.backgroundColor = UIColor.gray;
                
                // content inside article variables
                let articleImageFrameWidth = CGFloat((articleFrame.size.width / 2) - 20);
                
                
                let articleImageFrame = CGRect(x:0, y:0, width: articleImageFrameWidth, height: articleFrame.size.height);
                let articleImage = UIImageView(frame: articleImageFrame);
                articleImage.backgroundColor = UIColor.gray;
                articleImage.layer.cornerRadius = 20;
                
                let articleTitleFrame = CGRect(x: articleImageFrameWidth + 20, y: 10, width: articleFrame.size.width - articleImageFrameWidth - 20, height: 30);
                let articleTitle = UILabel(frame: articleTitleFrame);
                //articleTitle.numberOfLines = 1;
                articleTitle.adjustsFontSizeToFitWidth = true;
                articleTitle.minimumScaleFactor = 0.4;
                articleTitle.text = "Article Title Article Title";
                articleTitle.font = UIFont(name: "SFProText-Bold",size: 25);
                
                
                let articleDescriptionFrame = CGRect(x: articleImageFrameWidth + 20, y: 45, width: articleFrame.size.width - articleImageFrameWidth - 20, height: articleFrame.size.height - 50);
                let articleDescription = UILabel(frame: articleDescriptionFrame);
                articleDescription.numberOfLines = 3;
                articleDescription.text = "This is the content inside the article. What you are seeing is a preview of such article.";
                articleDescription.textColor = makeColor(r: 143, g: 135, b: 135);
                articleDescription.font = UIFont(name: "SFProText-Bold",size: 13);
                articleDescription.textAlignment = .left;
                
                articleButton.addSubview(articleImage);
                articleButton.addSubview(articleTitle);
                articleButton.addSubview(articleDescription);
                
                articleButton.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
                
                self.mainScrollView.addSubview(articleButton);
            }
            mainScrollView.contentSize = CGSize(width: articleFrame.size.width, height: 2*articleVerticalPadding+(articleFrame.size.height+articleVerticalPadding) * CGFloat(savedArticles.count)+85);
            mainScrollView.delegate = self;
        }
        else{
            mainScrollView.isHidden = true;
        }
    }
}
