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
                articleButton.backgroundColor = UIColor.white;
            
                let chevronWidth = CGFloat(22);
                let chevronFrame = CGRect(x: articleFrame.size.width-chevronWidth-15, y: 50, width: chevronWidth-5, height: chevronWidth);
                let chevronImage = UIImageView(frame: chevronFrame);
                chevronImage.image = UIImage(systemName: "chevron.right");
                chevronImage.tintColor = UIColor.gray;
                
                /*let imageViewFrame = CGRect(x: 0, y: 0, width: 100, height: 130);
                let imageView = UIImageView(frame: imageViewFrame);
                imageView.imgFromURL(sURL: )*/
                
                let articleCatagoryFrame = CGRect(x: 10, y: 8, width: 65, height: 20);
                let articleCatagory = UILabel(frame: articleCatagoryFrame);
                articleCatagory.backgroundColor = mainThemeColor;
                articleCatagory.text = savedArticles[aIndex].articleCatagory;
                articleCatagory.textAlignment = .center;
                articleCatagory.textColor = UIColor.white;
                articleCatagory.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                articleCatagory.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                
                let articleTitleFrame = CGRect(x: 10, y: 30, width: articleFrame.size.width-35-chevronFrame.size.width, height: 30);
                let articleTitle = UILabel(frame: articleTitleFrame);
                articleTitle.text = savedArticles[aIndex].articleTitle;
                articleTitle.contentMode = .left;
                //articleTitle.backgroundColor = UIColor.gray;
                articleTitle.font = UIFont(name: "SFProDisplay-Semibold", size: 18);
                
                let articleBodyFrame = CGRect(x: 10, y: 50, width: articleFrame.size.width-35-chevronFrame.size.width, height: articleFrame.size.height - 55);
                let articleBody = UILabel(frame: articleBodyFrame);
                articleBody.text = (savedArticles[aIndex].hasHTML == true ? parseHTML(s: savedArticles[aIndex].articleBody ?? "").string : savedArticles[aIndex].articleBody);
                articleBody.numberOfLines = 3;
                articleBody.font = UIFont(name:"SFProDisplay-Regular",size: 14);
                articleBody.textColor = UIColor.black;
                
                let timeStampWidth = CGFloat(100);
                let timeStampFrame = CGRect(x: articleFrame.size.width - 10 - timeStampWidth, y: 8, width: timeStampWidth, height: 20);
                let timeStamp = UILabel(frame: timeStampFrame);
                timeStamp.text = epochClass.epochToString(epoch: savedArticles[aIndex].articleUnixEpoch ?? -1);
                //timeStamp.text = "12 months ago";
                timeStamp.textAlignment = .right;
                timeStamp.font = UIFont(name:"SFProDisplay-Regular",size: 12);
                timeStamp.textColor = UIColor.darkGray;
                
                articleButton.addSubview(chevronImage);
                articleButton.addSubview(timeStamp);
                articleButton.addSubview(articleCatagory);
                articleButton.addSubview(articleTitle);
                articleButton.addSubview(articleBody);
                
                articleButton.layer.shadowColor = UIColor.black.cgColor;
                articleButton.layer.shadowOpacity = 0.2;
                articleButton.layer.shadowRadius = 5;
                articleButton.layer.shadowOffset = CGSize(width: 0 , height:3);
                
                articleButton.articleCompleteData = savedArticles[aIndex];
                articleButton.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
                
                self.mainScrollView.addSubview(articleButton);
            }
            mainScrollView.contentSize = CGSize(width: articleFrame.size.width, height: 2*articleVerticalPadding+(articleFrame.size.height+articleVerticalPadding) * CGFloat(savedArticles.count));
            mainScrollView.delegate = self;
 
        }
        else{
            mainScrollView.isHidden = true;
        }
    }
}
