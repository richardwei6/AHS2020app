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

class articlePageViewController: UIViewController, UIScrollViewDelegate{
    /*@IBOutlet weak var backButton: UIButton!
     @IBOutlet weak var articleText: UILabel!
     @IBOutlet weak var imageScrollView: UIScrollView!
     @IBOutlet weak var imagePageControl: UIPageControl!
     @IBOutlet weak var whiteBackground: UIImageView!*/    //@IBOutlet weak var notificationBellButton: UIButton!
    
    @IBOutlet weak var articleAuthor: UILabel!
    @IBOutlet weak var articleDate: UILabel!
    @IBOutlet weak var articleTitle: UILabel!
    @IBOutlet weak var mainScrollView: UIScrollView!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var whiteBG: UIView!
    @IBOutlet weak var articleBar: UIView!
    
    @IBOutlet weak var bookmarkButton: CustomUIButton!
    @IBOutlet weak var bookmarkOuter: CustomUIButton!
    
    var contentWidth: CGFloat = 0.0
    var imageFrame = CGRect(x: 0, y:0, width: 0, height: 0);
    var imageSize = 1;
    var articleContent: articleData?;
    
    
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
        dismiss(animated: true);
    }
    
    func setBookmarkColor(){
        if (articleContent?.articleID != nil && savedArticleClass.isSavedCurrentArticle(articleID: (articleContent?.articleID)!)){
            bookmarkButton.tintColor = mainThemeColor;
            bookmarkButton.isSelected = true;
        }
        else{
            bookmarkButton.tintColor = UIColor.white;
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
    
    // ------------
    // TODO: Fix issue where long text gets cut off *completed, thank u for the reminder XD -em&kim
    // TODO: Fix issue where the imagescrollview doesn't allow you to go to the third image on real devices -fixed
    // TODO: Fix issue on Storyboard where the Bookmark button gets blurry - use the system bookmark image and put a circular background behind it *completed
    // ------------
    override func viewDidLoad() {
        super.viewDidLoad();
        
        bookmarkButton.articleCompleteData = articleContent ?? articleData();
        bookmarkOuter.articleCompleteData = articleContent ?? articleData();
        
        setBookmarkColor();
        
        bookmarkOuter.setRoundedEdge(corners: [.topLeft, .topRight, .bottomLeft, .bottomRight], radius: 12);
        
       // mainScrollView.topAnchor.constraint(equalToSystemSpacingBelow: view.topAnchor, multiplier: 1).isActive = true;
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;

        
        
        articleText.text = articleContent?.articleBody;
        articleText.font = UIFont(name: articleText.font.fontName, size: CGFloat(fontSize));
        articleTitle.text = "Loading images..."; // see func viewdidappear
        articleDate.text = epochClass.epochToFormatedDateString(epoch: articleContent?.articleUnixEpoch ?? -1); // TODO: IMPLEMENT A FUNC TO GET INT TO STRING DATE
        articleAuthor.text = "By " + (articleContent?.articleAuthor ?? " NULL Author");
        
        
        articleBar.layer.cornerRadius = 3;
        whiteBG.layer.cornerRadius = 35;
        whiteBG.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]

        
        // TODO: add zoom feature here
        imageSize = articleContent?.articleImages?.count ?? 0;
        //print(imageSize);
        imagePageControl.numberOfPages = imageSize;
        imageFrame.size = imageScrollView.frame.size;
        imageFrame.size.width = UIScreen.main.bounds.size.width;
        for imageIndex in 0..<imageSize{
            imageFrame.origin.x = (imageFrame.size.width * CGFloat(imageIndex));
            
           // let imageZoom = UIScrollView(frame: imageFrame);
            let buttonImage = UIButton(frame: imageFrame);
            //imageView.imgFromURL(sURL: articleContent?.articleImages?[imageIndex] ?? "");
            //imageView.contentMode = .scaleAspectFit;
            
            buttonImage.imgFromURL(sURL: articleContent?.articleImages?[imageIndex] ?? "");
            buttonImage.imageView?.contentMode = .scaleAspectFit;
            buttonImage.isSelected = true;
            
            buttonImage.addTarget(self, action: #selector(toggleZoom), for: .touchUpInside);
            
            //print("\(imageView.image != nil)" + " - " + (articleContent?.articleImages?[imageIndex] ?? ""))
           // imageZoom.addSubview(imageView);
            //imageZoom.delegate = self;
            
            self.imageScrollView.addSubview(buttonImage);
        }
        imageScrollView.contentSize = CGSize(width: (imageFrame.size.width * CGFloat(imageSize)), height: imageScrollView.frame.size.height);
        imageScrollView.delegate = self;
        articleTitle.text = articleContent?.articleTitle; // set article title herer
        articleTitle.font = UIFont(name: articleTitle.font.fontName, size: CGFloat(fontSize));
    }

    
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        imagePageControl.currentPage = Int(imageScrollView.contentOffset.x / imageFrame.size.width);
    }
}
