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

class articlePageViewController: UIViewController, UIScrollViewDelegate, UINavigationControllerDelegate, UIGestureRecognizerDelegate{

    
    /*@IBOutlet weak var backButton: UIButton!
     @IBOutlet weak var articleText: UILabel!
     @IBOutlet weak var imageScrollView: UIScrollView!
     @IBOutlet weak var imagePageControl: UIPageControl!
     @IBOutlet weak var whiteBackground: UIImageView!*/    //@IBOutlet weak var notificationBellButton: UIButton!
    
    @IBOutlet var mainView: UIView!
    
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    @IBOutlet weak var bookmarkButton: CustomUIButton!
    
    @IBOutlet weak var articleCatagoryLabel: UILabel!
    
    @IBOutlet weak var shadowView: UIView!
    
    
    var contentWidth: CGFloat = 0.0
    var imageFrame = CGRect(x: 0, y:0, width: 0, height: 0);
    var imageSize = 1;
    var videoSize = 1;
    var articleContent: articleData?;
    var imageAvgColors = [Int:UIColor]();
    
    let imagePageControl = UIPageControl();
    let imageScrollView = UIScrollView();
    
    
    /// START DISMISS ON PAN
    var interactor: Interactor? = nil;
    let transition = CATransition();
    
    func transitionDismissal() {
        transition.duration = 0.2
        transition.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromLeft
        view.window?.layer.add(transition, forKey: nil)
        dismiss(animated: false)
    }
    
    
    @objc func gestureAction(_ sender: UIScreenEdgePanGestureRecognizer) {
        let percentThreshold: CGFloat = 0.4
        let translation = sender.translation(in: view)
        let fingerMovement = translation.x / view.bounds.width
        let rightMovement = fmaxf(Float(fingerMovement), 0.0)
        let rightMovementPercent = fminf(rightMovement, 1.0)
        let progress = CGFloat(rightMovementPercent)
        
        switch sender.state {
        case .began:
            
            interactor?.hasStarted = true
            dismiss(animated: true)
            
        case .changed:
            
            interactor?.shouldFinish = progress > percentThreshold
            interactor?.update(progress)
            
        case .cancelled:
            
            interactor?.hasStarted = false
            interactor?.cancel()
            
        case .ended:
            
            guard let interactor = interactor else { return }
            interactor.hasStarted = false
            interactor.shouldFinish
                ? interactor.finish()
                : interactor.cancel()
            
        default:
            break
        }
    }
    
    /// END DISMISS ON PAN
    
    @IBAction func saveArticle(sender: CustomUIButton){
        if (sender.articleCompleteData.articleID != nil){
            if (sender.isSelected == false){
                savedArticleClass.saveCurrArticle(articleID: sender.articleCompleteData.articleID!, article: sender.articleCompleteData);
            }
            else{
                savedArticleClass.removeCurrArticle(articleID: sender.articleCompleteData.articleID!);
            }
            sender.isSelected = !sender.isSelected;
            setBookmarkColor();
            resetUpArticles = true;
        }
    }
    
    @IBAction func exitArticle(_ sender: UIButton){
        imageAvgColors = [Int:UIColor]();
        transitionDismissal();
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
        // NewYorkSmall-MediumItalic, NewYorkMedium-Bold
        imageAvgColors = [Int:UIColor]();
        bookmarkButton.articleCompleteData = articleContent ?? articleData();
        
        bookmarkButton.tintColor = mainThemeColor;
        setBookmarkColor();
        
        let gestureRecognizer = UIScreenEdgePanGestureRecognizer(target: self, action: #selector(gestureAction));
        gestureRecognizer.edges = .left;
        gestureRecognizer.delegate = self;
        //view.addGestureRecognizer(gestureRecognizer);
        mainScrollView.addGestureRecognizer(gestureRecognizer);
        
        shadowView.layer.shadowColor = UIColor.black.cgColor;
        shadowView.layer.shadowOpacity = 0.05;
        shadowView.layer.shadowOffset = CGSize(width: 0 , height: 5);
        
        articleCatagoryLabel.text = articleContent?.articleCatagory ?? "NO Cata.";
        articleCatagoryLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight, .topLeft, .topRight], radius: 5);
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        var nextY = CGFloat(10);
        let padding = CGFloat(15);
        let universalWidth = UIScreen.main.bounds.width - 2 * padding;
        
        //nextY += 7;
        let articleTitleText = articleContent?.articleTitle;
        let titleFont = UIFont(name: "NewYorkMedium-Bold", size: CGFloat(fontSize+8));
        let articleTitleFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleTitleText?.getHeight(withConstrainedWidth: universalWidth, font: titleFont!) ?? 0);
        let articleTitle = UILabel(frame: articleTitleFrame);
        articleTitle.text = articleTitleText; // set article title herer
        articleTitle.font = titleFont;
        articleTitle.numberOfLines = 0;
        mainScrollView.addSubview(articleTitle);
        nextY += articleTitleFrame.height + 7;
        
        if ((articleContent?.articleVideoIDs?.count ?? 0) + (articleContent?.articleImages?.count ?? 0) > 0){ // bulletin
            let imageScrollViewFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: 250);
            imageScrollView.frame = imageScrollViewFrame;
            
            imageSize = articleContent?.articleImages?.count ?? 0;
            videoSize = articleContent?.articleVideoIDs?.count ?? 0;
            imageScrollView.backgroundColor = UIColor.clear;
            
            imageFrame.size = imageScrollView.frame.size;
            var origX = CGFloat(0);
            for videoIndex in 0..<videoSize{
                imageFrame.origin.x = origX;
                let videoPlayer = YTPlayerView(frame: imageFrame);
                videoPlayer.load(withVideoId: articleContent?.articleVideoIDs?[videoIndex] ?? "");
                imageScrollView.addSubview(videoPlayer);
                origX += imageFrame.size.width;
            }
            for imageIndex in 0..<imageSize{
                imageFrame.origin.x = origX;
                let buttonImage = UIButton(frame: imageFrame);
                buttonImage.imgFromURL(sURL: articleContent?.articleImages?[imageIndex] ?? "");
                buttonImage.imageView?.contentMode = .scaleAspectFill;
                buttonImage.imageView?.image?.getColors({ (colors) -> Void in
                    self.imageAvgColors[imageIndex+self.videoSize] = colors?.primary ?? UIColor.lightGray;
                    if (imageIndex == 0){
                        self.imageScrollView.backgroundColor = self.imageAvgColors[self.videoSize];
                    }
                });
                buttonImage.isSelected = false;
                buttonImage.addTarget(self, action: #selector(toggleZoom), for: .touchUpInside);
                imageScrollView.addSubview(buttonImage);
                origX += imageFrame.size.width;
            }
            imageScrollView.contentSize = CGSize(width: origX, height: imageScrollView.frame.size.height);
            imageScrollView.delegate = self;
            imageScrollView.layer.cornerRadius = 5;
            imageScrollView.isPagingEnabled = true;
            imageScrollView.showsHorizontalScrollIndicator = false;
            
            nextY += imageScrollViewFrame.size.height;
            
            if (imageSize + videoSize > 1){
                imagePageControl.currentPage = 0;
                imagePageControl.numberOfPages = imageSize + videoSize;
                imagePageControl.center = CGPoint(x: UIScreen.main.bounds.width / 2, y: nextY + 12);
                imagePageControl.pageIndicatorTintColor = UIColor.lightGray;
                imagePageControl.currentPageIndicatorTintColor = UIColor.black;
                mainScrollView.addSubview(imagePageControl);
                nextY += 20;
            }
            
            mainScrollView.addSubview(imageScrollView);
        }
        
        
        nextY += 7;
        if (articleContent?.articleAuthor != nil){
            let articleAuthorText = "By " + (articleContent?.articleAuthor ?? "No Author");
            let articleAuthorFont = UIFont(name: "NewYorkSmall-MediumItalic", size: CGFloat(fontSize-1));
            let articleAuthorFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleAuthorText.getHeight(withConstrainedWidth: universalWidth, font: articleAuthorFont!))
            let articleAuthor = UILabel(frame: articleAuthorFrame);
            articleAuthor.text = articleAuthorText;
            articleAuthor.font = articleAuthorFont;
            articleAuthor.textColor = makeColor(r: 112, g: 112, b: 112);
            articleAuthor.numberOfLines = 0;
            mainScrollView.addSubview(articleAuthor);
            nextY += articleAuthorFrame.size.height+3;
        }
        
        let articleDateText = epochClass.epochToFormatedDateString(epoch: articleContent?.articleUnixEpoch ?? -1);
        let articleDateFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleDateText.getHeight(withConstrainedWidth: universalWidth, font: UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize-3))!));
        let articleDate = UILabel(frame: articleDateFrame);
        articleDate.text = articleDateText;
        articleDate.font = UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize-3));
        articleDate.textColor = makeColor(r: 112, g: 112, b: 112);
        articleDate.numberOfLines = 0;
        mainScrollView.addSubview(articleDate);
        nextY += articleDateFrame.size.height;
        
        nextY += 7;
        let articleBodyText = (articleContent?.hasHTML == true ? parseHTML(s: articleContent?.articleBody ?? "") : NSAttributedString(string: articleContent?.articleBody ?? ""));
        let articleBodyFrame = CGRect(x: padding, y: nextY, width: universalWidth, height: articleBodyText.string.getHeight(withConstrainedWidth: universalWidth, font: UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize))!));
        let articleBody = UITextView(frame: articleBodyFrame);
        articleBody.attributedText = articleBodyText;
        articleBody.font = UIFont(name: "SFProDisplay-Regular", size: CGFloat(fontSize));
        articleBody.textColor = makeColor(r: 33, g: 33, b: 33);
        articleBody.isScrollEnabled = false;
        articleBody.isEditable = false;
        articleBody.tintColor = UIColor.systemBlue;
        articleBody.contentInset = UIEdgeInsets(top: -7, left: -5, bottom: 0, right: 0);
        articleBody.sizeToFit();
        mainScrollView.addSubview(articleBody);
        nextY += articleBody.frame.size.height;
        mainScrollView.contentSize = CGSize(width: universalWidth, height: nextY + 30);
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if (articleContent?.articleAuthor != nil){
            imagePageControl.currentPage = Int(round(imageScrollView.contentOffset.x / imageFrame.size.width));
            UIScrollView.animate(withDuration: 0.3, delay: 0, options: .allowUserInteraction, animations: {self.imageScrollView.backgroundColor = self.imageAvgColors[self.imagePageControl.currentPage] != nil ? self.imageAvgColors[self.imagePageControl.currentPage] : UIColor.lightGray;}, completion: nil);
        }
    }
}
