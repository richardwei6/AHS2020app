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
    @IBOutlet weak var articleText: UITextView!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var contentView: UIView!
    
    @IBOutlet weak var bookmarkButton: CustomUIButton!
    
    @IBOutlet weak var articleCatagoryLabel: UILabel!
    
    var contentWidth: CGFloat = 0.0
    var imageFrame = CGRect(x: 0, y:0, width: 0, height: 0);
    var imageSize = 1;
    var articleContent: articleData?;
    var imageAvgColors = [Int:UIColor]();
    
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
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        imageAvgColors = [Int:UIColor]();
        bookmarkButton.articleCompleteData = articleContent ?? articleData();
        
        setBookmarkColor();
        
        articleCatagoryLabel.text = articleContent?.articleCatagory ?? "NO Cata.";
        articleCatagoryLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight, .topLeft, .topRight], radius: 5);
        
       // mainScrollView.topAnchor.constraint(equalToSystemSpacingBelow: view.topAnchor, multiplier: 1).isActive = true;
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;

        
        
        articleText.attributedText = (articleContent?.hasHTML == true ? parseHTML(s: articleContent?.articleBody ?? "") : NSAttributedString(string: articleContent?.articleBody ?? ""));
        articleText.font = UIFont(name: articleText.font!.fontName, size: CGFloat(fontSize));
        articleText.sizeToFit();
        articleText.isScrollEnabled = false;
        articleText.tintColor = UIColor.systemBlue;
       // articleText.translatesAutoresizingMaskIntoConstraints = true;
        
        
        articleTitle.text = articleContent?.articleTitle; // set article title herer
        articleTitle.font = UIFont(name: articleTitle.font.fontName, size: CGFloat(fontSize+5));
        articleDate.text = epochClass.epochToFormatedDateString(epoch: articleContent?.articleUnixEpoch ?? -1); // TODO: IMPLEMENT A FUNC TO GET INT TO STRING DATE
        articleAuthor.text = "By " + (articleContent?.articleAuthor ?? " No Author");
        articleAuthor.font = UIFont(name: articleAuthor.font.fontName, size: CGFloat(fontSize));

        // TODO: add zoom feature here
        imageSize = articleContent?.articleImages?.count ?? 0;
        //print(imageSize);
        
        imagePageControl.numberOfPages = imageSize;
        imageFrame.size = imageScrollView.frame.size;
        imageFrame.size.width = UIScreen.main.bounds.size.width - 42;
        for imageIndex in 0..<imageSize{
            imageFrame.origin.x = (imageFrame.size.width * CGFloat(imageIndex));
            
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
            self.imageScrollView.addSubview(buttonImage);
        }
        imageScrollView.contentSize = CGSize(width: (imageFrame.size.width * CGFloat(imageSize)), height: imageScrollView.frame.size.height);
        imageScrollView.delegate = self;
        imageScrollView.layer.cornerRadius = 10;
    }

    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        imagePageControl.currentPage = Int(imageScrollView.contentOffset.x / imageFrame.size.width);
        UIScrollView.animate(withDuration: 0.3, delay: 0, options: .allowUserInteraction, animations: {self.imageScrollView.backgroundColor = self.imageAvgColors[self.imagePageControl.currentPage] != nil ? self.imageAvgColors[self.imagePageControl.currentPage] : UIColor.lightGray;}, completion: nil);
    }
}
