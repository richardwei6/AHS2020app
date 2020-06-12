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
    
    
    @objc func openArticle(sender: CustomUIButton){
       // print("Button pressed");
       let articleDataDict: [String: articleData] = ["articleContent" : sender.articleCompleteData];
       NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        let savedArticles =  savedArticleClass.getSavedArticles();
        
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
                
                let articleButton = CustomUIButton(frame: articleFrame);
                //articleButton.backgroundColor = UIColor.gray;
                
                // content inside article variables
                let articleImageFrameWidth = CGFloat((articleFrame.size.width / 2) - 20);
                
                
                let articleImageFrame = CGRect(x:0, y:0, width: articleImageFrameWidth, height: articleFrame.size.height);
                let articleImage = UIImageView(frame: articleImageFrame);
                articleImage.backgroundColor = makeColor(r: 143, g: 142, b: 142);
                articleImage.imgFromURL(sURL: savedArticles[aIndex].articleImages?[0] ?? "");
                articleImage.setRoundedEdge(corners: [.topLeft, .topRight, .bottomLeft, .bottomRight], radius: 20);
                articleImage.contentMode = .scaleAspectFit;
                
                let articleTitleFrame = CGRect(x: articleImageFrameWidth + 20, y: 10, width: articleFrame.size.width - articleImageFrameWidth - 20, height: 30);
                let articleTitle = UILabel(frame: articleTitleFrame);
                //articleTitle.numberOfLines = 1;
                articleTitle.adjustsFontSizeToFitWidth = true;
                articleTitle.minimumScaleFactor = 0.4;
                articleTitle.text = savedArticles[aIndex].articleTitle; // DATA
                articleTitle.font = UIFont(name: "SFProText-Bold",size: 25);
                
                
                let articleDescriptionFrame = CGRect(x: articleImageFrameWidth + 20, y: 45, width: articleFrame.size.width - articleImageFrameWidth - 20, height: articleFrame.size.height - 50);
                let articleDescription = UILabel(frame: articleDescriptionFrame);
                articleDescription.numberOfLines = 3;
                articleDescription.text = savedArticles[aIndex].articleBody; // DATA
                articleDescription.textColor = makeColor(r: 143, g: 135, b: 135);
                articleDescription.font = UIFont(name: "SFProText-Bold",size: 13);
                articleDescription.textAlignment = .left;
                
                articleButton.articleCompleteData = savedArticles[aIndex];
                
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
