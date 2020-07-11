//
//  bulletinPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import Firebase
import FirebaseDatabase



class bulletinClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {
    
    @IBOutlet weak var filterScrollView: UIScrollView!
    @IBOutlet weak var bulletinScrollView: UIScrollView!
    
    
    @IBOutlet weak var filterScrollViewHeightContraint: NSLayoutConstraint!
    
    
    let refreshControl = UIRefreshControl();
    let seniorYearConst = "21";
    
    // padding variables
    let articleHorizontalPadding = CGFloat(10);
    let articleVerticalPadding = CGFloat(10);
    let articleVerticalSize = CGFloat(130);
    
    
    let filterSize = 6;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    
    let filterIconPicturePath = ["Group 51.png","Path 275.png","Group 34.png","Path 276.png","Path 277.png"];
    let filterName = ["Seniors", "Colleges", "Events", "Athletics", "Reference", "Others"];
    
    var selectedFilters: [Bool] = [false, false, false, false, false, false]; // selected types in this order - seniors, colleges, events, athletics, reference, and others
    
    var totalArticles = [bulletinArticleData]();
    
    // icon stuff
    var iconViewFrame: CGRect?;
    var filterScrollViewMaxHeight: CGFloat?;
    var filterScrollViewMinHeight: CGFloat?;
    
    func getBulletinArticleData() {
        setUpConnection();
        if (internetConnected){
            print("ok -------- loading articles - bulletin");
            
            bulletinArticleList = [[bulletinArticleData]](repeating: [bulletinArticleData](), count: 6);
            for i in 0..<6{
                var s: String; // path inside homepage
                switch i {
                case 0: // seniors
                    s = "seniors";
                    break;
                case 1: // colleges
                    s = "colleges";
                    break;
                case 2: // events
                    s = "events";
                    break;
                case 3: // athletics
                    s = "athletics";
                    break;
                case 4: // reference
                    s = "reference";
                    break;
                case 5: // others
                    s = "others";
                    break;
                default:
                    s = "";
                    break;
                }
                
                ref.child("bulletin").child(s).observeSingleEvent(of: .value) { (snapshot) in
                  //  print(s);
                    
                  //  print(snapshot.childrenCount)
                    let enumerator = snapshot.children;
                    var temp = [bulletinArticleData](); // temporary array
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        //print(article);
                        
                        
                        let enumerator = article.children;
                        var singleArticle = bulletinArticleData();
                        
                        singleArticle.articleID = article.key as! String;
                        
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                            
                            
                            if (articleContent.key == "articleAuthor"){
                                singleArticle.articleAuthor = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleBody"){
                                singleArticle.articleBody = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleUnixEpoch"){
                                singleArticle.articleUnixEpoch = articleContent.value as? Int64;
                            }
                            else if (articleContent.key == "articleImages"){
                                
                                var tempImage = [String]();
                                let imageIt = articleContent.children;
                                while let image = imageIt.nextObject() as? DataSnapshot{
                                    tempImage.append(image.value as! String);
                                }
                                //print(tempImage)
                                singleArticle.articleImages = tempImage;
                            }
                            else if (articleContent.key == "articleTitle"){
                                
                                singleArticle.articleTitle = articleContent.value as? String;
                            }
                            //print(articleContent.key as? NSString);
                            
                        }
                        // print("append to main")
                        // print(temp.count);
                        //print(singleArticle.articleTitle);
                        //print(singleArticle.articleImages);
                        singleArticle.articleType = i;
                        temp.append(singleArticle);
                        
                    }
                    //print("append to main - ")
                    //print(temp)
                    bulletinArticleList[i] = temp;
                    self.generateBulletin();
                    self.refreshControl.endRefreshing();
                    self.addRefreshCTRL();
                };
                
            }
            
        }
        else{
            print("no network detected - bulletin");
            refreshControl.endRefreshing();
            addRefreshCTRL();
        }
        
    }
    
    @objc func openArticle(sender: CustomUIButton){
        print("Button pressed");
        let articleDataDict: [String: articleData] = ["articleContent" : sender.articleCompleteData];
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
    }
    
    @objc func addFilter(sender: CustomUIButton){
        sender.isSelected = !sender.isSelected;
        selectedFilters[sender.articleIndex] = sender.isSelected;
        setUpFilters();
        generateBulletin();
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
    }
    
    func removeAllSubViews(sender: CustomUIButton){
        for view in sender.subviews{
            view.removeFromSuperview();
        }
    }
    
    
    /*func generateIconImage(iconView: CustomUIButton){
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
    }*/
    
    
    func filterArticles() -> [bulletinArticleData]{
        var copy = [bulletinArticleData]();
        
        for i in 0..<totalArticles.count{
            if (selectedFilters[totalArticles[i].articleType] == true){
                copy.append(totalArticles[i]);
            }
        }
        return copy.count == 0 ?totalArticles:copy;
    }
    
    func addRefreshCTRL(){
        refreshControl.addTarget(self, action: #selector(refreshBulletin), for: UIControl.Event.valueChanged);
        bulletinScrollView.addSubview(refreshControl);
        bulletinScrollView.isScrollEnabled = true;
        bulletinScrollView.alwaysBounceVertical = true;
    }
    
    
    func generateBulletin(){ // TODO: implement filter ---------
        // set up bulletin
        
        // remove all prev views
        if (bulletinArticleList[0].count > 0 && bulletinArticleList[1].count > 0 && bulletinArticleList[2].count > 0 && bulletinArticleList[3].count > 0 && bulletinArticleList[4].count > 0 && bulletinArticleList[5].count > 0){
      
            totalArticles = [bulletinArticleData]();
            for i in 0...5{
                for c in bulletinArticleList[i]{
                    totalArticles.append(c);
                }
            }
            
            for subview in bulletinScrollView.subviews{
                subview.removeFromSuperview();
            }
            
            var currentArticles = filterArticles();
            
            var bulletinSize = currentArticles.count;
            var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
            
            bulletinFrame.size.height = articleVerticalSize;
            bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
            
            let imageArticleSize = CGFloat(35);
            
            for aIndex in 0..<bulletinSize{
                bulletinFrame.origin.x = articleHorizontalPadding;
                bulletinFrame.origin.y = articleVerticalPadding+((bulletinFrame.size.height+articleVerticalPadding)*CGFloat(aIndex));
                let articleButton = UIView(frame: bulletinFrame);
                articleButton.backgroundColor = UIColor.white;
                
                
                
                // content inside button
                let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding), height: bulletinFrame.size.height - 10);
                let mainView = CustomUIButton(frame: mainViewFrame);
                
                
                
                let articleTitleFrame = CGRect(x: 0, y : 17, width: UIScreen.main.bounds.size.width - articleHorizontalPadding - 55, height: 34);
                let articleTitleText = UILabel(frame: articleTitleFrame);
                articleTitleText.text = currentArticles[aIndex].articleTitle; // insert title text here ------ temporary
                articleTitleText.font =  UIFont(name: "SFProText-Bold",size: 30);
                //articleTitleText.numberOfLines = 1;
                //articleTitleText.backgroundColor = UIColor.gray;
                articleTitleText.adjustsFontSizeToFitWidth = true;
                articleTitleText.minimumScaleFactor = 0.4;
                
                
                
                let articleBodyFrame = CGRect(x: 0, y: 44, width: mainViewFrame.size.width, height: mainViewFrame.size.height - 50);
                let articleBodyText = UILabel(frame: articleBodyFrame);
                articleBodyText.text = currentArticles[aIndex].articleBody;// insert body text here ------- temporary
                articleBodyText.numberOfLines = 4;
                articleBodyText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
                
                
                
                mainView.addSubview(articleTitleText);
                //mainView.addSubview(dateText);
                mainView.addSubview(articleBodyText);
                
                let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 95, y : 5, width: 100, height: 25);
                let dateText = UILabel(frame: dateTextFrame);
                //dateText.text = epochClass.epochToString(epoch: currentArticles[aIndex].articleUnixEpoch ?? -1); // insert date here -------- temporary
                dateText.text = "12 months ago";
                dateText.textColor = makeColor(r: 156, g: 0, b: 0);
                dateText.textAlignment = .right;
                dateText.font = UIFont(name: "SFProDisplay-Regular", size: 12);
                
                
                articleButton.addSubview(dateText);
                articleButton.addSubview(mainView);
                
                articleButton.layer.shadowColor = UIColor.black.cgColor;
                articleButton.layer.shadowOpacity = 0.1;
                articleButton.layer.shadowRadius = 5;
                articleButton.layer.shadowOffset = CGSize(width: 0 , height:4);
                
                mainView.articleCompleteData = bulletinDataToarticleData(data: currentArticles[aIndex]);
                
                mainView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
                self.bulletinScrollView.addSubview(articleButton);
            }
            bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width, height: 2*articleVerticalPadding+(bulletinFrame.size.height+articleVerticalPadding)*CGFloat(bulletinSize));
            bulletinScrollView.delegate = self;
            addRefreshCTRL();
        }
    }
    
    @objc func refreshBulletin(){
        print("refresh");
        // add func to load data
        getBulletinArticleData();
    }
    
    func setUpFilters(){
        
        for view in filterScrollView.subviews{
            view.removeFromSuperview();
        }
        
        let betweenMargin = CGFloat(3);
        
        let buttonFrameWidth = CGFloat(80);
        let filterFrameHorizontalPadding = CGFloat(20);
        
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterScrollView.frame.size.height;
        filterFrame.size.width = buttonFrameWidth; //
        
        let shadowViewHeight = CGFloat(1);
        var originX = CGFloat(16);
        
        for buttonIndex in 0..<filterSize{
            
            filterFrame.origin.x = originX;
            let filterButton = CustomUIButton(frame: filterFrame);
            
            //filterButton.backgroundColor = UIColor.gray;
            filterButton.setTitle(filterName[buttonIndex], for: .normal);
            filterButton.setTitleColor(UIColor.black, for: .normal);
            filterButton.titleLabel?.font = UIFont(name: "SFProDisplay-Regular", size: 20);
            filterButton.contentVerticalAlignment = .top;
            filterButton.sizeToFit();
            //SFProText-Bold, SFProDisplay-Regular, SFProDisplay-Semibold, SFProDisplay-Black
            
            
            //mainView.backgroundColor = UIColor.red;
            /*let filterTextFrame = CGRect(x: 0, y: 0, width: filterFrame.size.width, height: 30);
             let filterText = UILabel(frame: filterTextFrame);
             filterText.text = filterName[buttonIndex];
             filterText.textAlignment = .center;
             
             filterButton.addSubview(filterText);*/
            
            filterFrame.size.width = filterButton.frame.size.width;
            let selectedBarHeight = CGFloat(2);
            let selectedBarFrame = CGRect(x: originX, y: filterFrame.size.height-selectedBarHeight-shadowViewHeight, width: filterFrame.size.width, height: selectedBarHeight);
            let selectedBar = UIView(frame: selectedBarFrame);
            selectedBar.backgroundColor = makeColor(r: 159, g: 12, b: 12);
            selectedBar.layer.cornerRadius = 1;
            selectedBar.isHidden = !selectedFilters[buttonIndex];
            
            filterButton.isSelected = selectedFilters[buttonIndex];
            
            filterButton.articleIndex = buttonIndex;
            filterButton.addTarget(self, action: #selector(self.addFilter), for: .touchUpInside);
            
            self.filterScrollView.addSubview(selectedBar);
            self.filterScrollView.addSubview(filterButton);
            originX += filterButton.frame.size.width + filterFrameHorizontalPadding;
        }
        filterScrollView.contentSize = CGSize(width: originX, height: filterScrollView.frame.size.height);
        
        let shadowViewFrame = CGRect(x: -500, y: filterScrollView.frame.size.height-shadowViewHeight, width: filterScrollView.contentSize.width+1000, height: shadowViewHeight);
        let shadowView = UIView(frame: shadowViewFrame);
        shadowView.backgroundColor = UIColor.black;
        filterScrollView.addSubview(shadowView);
        filterScrollView.delegate = self;
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();

        setUpFilters();
        // set up bulletin for the first time before any filters
        getBulletinArticleData();
        
    }
    
}
