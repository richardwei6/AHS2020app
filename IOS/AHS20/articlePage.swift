//
//  File.swift
//  AHS20
//
//  Created by Richard Wei on 4/21/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import youtube_ios_player_helper

class articlePageViewController: UIViewController, UIScrollViewDelegate{
    /*@IBOutlet weak var backButton: UIButton!
     @IBOutlet weak var articleText: UILabel!
     @IBOutlet weak var imageScrollView: UIScrollView!
     @IBOutlet weak var imagePageControl: UIPageControl!
     @IBOutlet weak var whiteBackground: UIImageView!*/    //@IBOutlet weak var notificationBellButton: UIButton!
    
    
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    @IBOutlet weak var bookmarkButton: CustomUIButton!
    
    @IBOutlet weak var articleCatagoryLabel: UILabel!
    
    var contentWidth: CGFloat = 0.0
    var imageFrame = CGRect(x: 0, y:0, width: 0, height: 0);
    var imageSize = 1;
    var videoSize = 1;
    var articleContent: articleData?;
    var imageAvgColors = [Int:UIColor]();
    
    let imagePageControl = UIPageControl();
    let imageScrollView = UIScrollView();
    
    @IBAction func saveArticle(sender: CustomUIButton){
        print("Bookmark");
        if (sender.articleCompleteData.articleID != nil){
            if (sender.isSelected == false){
                // saveCurrentArticle(articleID: sender.articleID ?? ""); // TODO: change ?? to ! instead
                savedArticleClass.saveCurrArticle(articleID: sender.articleCompleteData.articleID!, article: sender.articleCompleteData);
            }
            else{
                //  removeCurrentArticle(articleID: sender.articleID ?? ""); // TODO: change ?? to ! instead
                savedArticleClass.removeCurrArticle(articleID: sender.articleCompleteData.articleID!);
            }
            // TODO: FIX
            sender.isSelected = !sender.isSelected;
            setBookmarkColor();
            resetUpArticles = true;
        }
    }
    
    @IBAction func exitArticle(_ sender: UIButton){
        imageAvgColors = [Int:UIColor]();
        dismiss(animated: true);
    }
    
    func setBookmarkColor(){
        if (articleContent?.articleID != nil && savedArticleClass.isSavedCurrentArticle(articleID: (articleContent?.articleID)!)){
            bookmarkButton.setImage(UIImage(systemName: "bookmark.fill"), for: .normal);
            
            bookmarkButton.isSelected = true;
        }
        else{
            bookmarkButton.setImage(UIImage(systemName: "bookmark"), for: .normal);
            bookmarkButton.isSelected = false;
        }
    }
    
    @objc func toggleZoom(sender: UIButton){
        if (sender.isSelected){
            sender.imageView?.contentMode = .scaleAspectFill;
        }
        else{
            sender.imageView?.contentMode = .scaleAspectFit;
        }
        sender.isSelected = !sender.isSelected;
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        imageAvgColors = [Int:UIColor]();
        bookmarkButton.articleCompleteData = articleContent ?? articleData();
        
        bookmarkButton.tintColor = mainThemeColor;
        setBookmarkColor();
                
        articleCatagoryLabel.text = articleContent?.articleCatagory ?? "NO Cata.";
        articleCatagoryLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight, .topLeft, .topRight], radius: 5);
        
        // mainScrollView.topAnchor.constraint(equalToSystemSpacingBelow: view.topAnchor, multiplier: 1).isActive = true;
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        var nextY = CGFloat(0);
        let padding = CGFloat(12);
        let universalWidth = UIScreen.main.bounds.width - 2 * padding;
        
        
        if (articleContent?.articleAuthor != nil){ // bulletin
            let imageScrollViewFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: 250);
            imageScrollView.frame = imageScrollViewFrame;
            imageSize = articleContent?.articleImages?.count ?? 0;
            videoSize = articleContent?.articleVideoIDs?.count ?? 0;
            imageScrollView.backgroundColor = UIColor.lightGray;
            
            //imagePageControl.numberOfPages = imageSize + videoSize;
            imageFrame.size = imageScrollView.frame.size;
            var origX = CGFloat(0);
            for imageIndex in 0..<imageSize{
                imageFrame.origin.x = origX;
                
                // let imageZoom = UIScrollView(frame: imageFrame);
                let buttonImage = UIButton(frame: imageFrame);
                //imageView.imgFromURL(sURL: articleContent?.articleImages?[imageIndex] ?? "");
                //imageView.contentMode = .scaleAspectFit;
                buttonImage.imgFromURL(sURL: articleContent?.articleImages?[imageIndex] ?? "");
                buttonImage.imageView?.contentMode = .scaleAspectFill;
                buttonImage.imageView?.image?.getColors({ (colors) -> Void in
                    self.imageAvgColors[imageIndex] = colors?.primary ?? UIColor.lightGray;
                    if (imageIndex == 0){
                        self.imageScrollView.backgroundColor = self.imageAvgColors[0];
                    }
                });
                buttonImage.isSelected = false;
                buttonImage.addTarget(self, action: #selector(toggleZoom), for: .touchUpInside);
                imageScrollView.addSubview(buttonImage);
                origX += imageFrame.size.width;
            }
            for videoIndex in 0..<videoSize{
                imageFrame.origin.x = origX;
                let videoPlayer = YTPlayerView(frame: imageFrame);
                videoPlayer.load(withVideoId: articleContent?.articleVideoIDs?[videoIndex] ?? "");
                imageScrollView.addSubview(videoPlayer);
                origX += imageFrame.size.width;
            }
            imageScrollView.contentSize = CGSize(width: origX, height: imageScrollView.frame.size.height);
            imageScrollView.delegate = self;
            imageScrollView.layer.cornerRadius = 5;
            imageScrollView.isPagingEnabled = true;
            imageScrollView.showsHorizontalScrollIndicator = false;
            
           // imagePageControl.frame = CGRect(x: imagePageControl.size(forNumberOfPages: imageSize + videoSize))

            imagePageControl.currentPage = 0;
            imagePageControl.numberOfPages = imageSize + videoSize;
            imagePageControl.center = CGPoint(x: UIScreen.main.bounds.width / 2, y: imageScrollViewFrame.size.height - 15);
            
            mainScrollView.addSubview(imageScrollView);
            mainScrollView.addSubview(imagePageControl);
            nextY += imageScrollViewFrame.size.height;
        }
        
        nextY += 7;
        let articleTitleText = articleContent?.articleTitle;
        let articleTitleFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleTitleText?.getHeight(withConstrainedWidth: universalWidth, font: UIFont(name: "SFProDisplay-Semibold", size: CGFloat(fontSize+5))!) ?? 0);
        let articleTitle = UILabel(frame: articleTitleFrame);
        articleTitle.text = articleTitleText; // set article title herer
        articleTitle.font = UIFont(name: "SFProDisplay-Semibold", size: CGFloat(fontSize+5));
        articleTitle.numberOfLines = 0;
        mainScrollView.addSubview(articleTitle);
        nextY += articleTitleFrame.height;
        
        nextY += 5;
        if (articleContent?.articleAuthor != nil){
            let articleAuthorText = "By " + (articleContent?.articleAuthor ?? "No Author");
            let articleAuthorFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleAuthorText.getHeight(withConstrainedWidth: universalWidth, font: UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize-3))!))
            let articleAuthor = UILabel(frame: articleAuthorFrame);
            articleAuthor.text = articleAuthorText;
            articleAuthor.font = UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize-3));
            articleAuthor.numberOfLines = 0;
            mainScrollView.addSubview(articleAuthor);
            nextY += articleAuthorFrame.size.height;
        }
        
        let articleDateText = epochClass.epochToFormatedDateString(epoch: articleContent?.articleUnixEpoch ?? -1);
        let articleDateFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleDateText.getHeight(withConstrainedWidth: universalWidth, font: UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize-3))!));
        let articleDate = UILabel(frame: articleDateFrame);
        articleDate.text = articleDateText;
        articleDate.font = UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize-3));
        articleDate.numberOfLines = 0;
        mainScrollView.addSubview(articleDate);
        nextY += articleDateFrame.size.height;
        
        nextY += 5;
     //   nextY += padding;
        let articleBodyText = (articleContent?.hasHTML == true ? parseHTML(s: articleContent?.articleBody ?? "") : NSAttributedString(string: articleContent?.articleBody ?? ""));
        let articleBodyFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleBodyText.string.getHeight(withConstrainedWidth: universalWidth, font: UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize))!));
        let articleBody = UITextView(frame: articleBodyFrame);
        articleBody.attributedText = articleBodyText;
        articleBody.font = UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize));
        articleBody.isScrollEnabled = false;
       // articleBody.isSelectable = false;
        articleBody.isEditable = false;
        articleBody.tintColor = UIColor.systemBlue;
        articleBody.contentInset = UIEdgeInsets(top: -7, left: -5, bottom: 0, right: 0);
        articleBody.sizeToFit();
        //articleBody.backgroundColor = UIColor.lightGray;
        mainScrollView.addSubview(articleBody);
        nextY += articleBody.frame.size.height;
        // articleText.translatesAutoresizingMaskIntoConstraints = true;
        mainScrollView.contentSize = CGSize(width: universalWidth, height: nextY + 30);
        
         /*articleTitle.text = articleContent?.articleTitle; // set article title herer
         articleTitle.font = UIFont(name: articleTitle.font.fontName, size: CGFloat(fontSize+5));
         articleDate.text = epochClass.epochToFormatedDateString(epoch: articleContent?.articleUnixEpoch ?? -1); // TODO: IMPLEMENT A FUNC TO GET INT TO STRING DATE
         articleAuthor.text = "By " + (articleContent?.articleAuthor ?? " No Author");
         articleAuthor.font = UIFont(name: articleAuthor.font.fontName, size: CGFloat(fontSize));
         
         // TODO: add zoom feature here
         */
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if (articleContent?.articleAuthor != nil){
            
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if (articleContent?.articleAuthor != nil){
        imagePageControl.currentPage = Int(round(imageScrollView.contentOffset.x / imageFrame.size.width));
        UIScrollView.animate(withDuration: 0.3, delay: 0, options: .allowUserInteraction, animations: {self.imageScrollView.backgroundColor = self.imageAvgColors[self.imagePageControl.currentPage] != nil ? self.imageAvgColors[self.imagePageControl.currentPage] : UIColor.lightGray;}, completion: nil);
        }
    }
}
