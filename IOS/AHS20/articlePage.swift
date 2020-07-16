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
    
    func parseHref(h: String) -> (String, URL){ // txt, https://
        var i = 0;
        var p = false, content = false;
        var a:String = "", b:String = "";
        while i < h.count{
            if (h[i] == "\""){
                p = !p;
                if (p == false){
                    content = true;
                }
            }
            else if (p == true){
                a += h[i];
            }
            else if (content == true){
                if (h[i] == "<"){
                    content = false;
                }
                else if (h[i] != ">"){
                    b += h[i];
                }
            }
            i+=1;
        }
        return (b, (URL(string: "\(a)") ?? URL(string: "https://ahs.ausd.net")!) as URL); // change ! to ??
    }
    
    func replaceString(t: String) -> (String, [((Int, Int),URL)]) { // replace all <a href = "https://">txt</a> with txt
        var out = [((Int, Int), URL)]();
        var s = t;
        var i = 0;
        while i < s.count{
            if (s[i] == "<"){
                var j = i;
                while (j < s.count){ // find end
                    if (s[j] == ">" && s[j-1] == "a"){
                        break;
                    }
                    j+=1;
                }
                let m = parseHref(h: s[i..<j+1]);
                // replace [i,j) with m.0
                let range = s.index(s.startIndex, offsetBy: i)..<s.index(s.startIndex, offsetBy: j+1);
               // print("old string - \(s)")
                s.replaceSubrange(range, with: m.0);
              //  print("new string - \(s) with - \(i) to \(i+m.0.count-1)");
                out.append(((i, i + m.0.count-1), m.1));
                i += m.0.count;
            }
            else{
                i+=1;
            }
        }
        return (s, out);
    }
    
    func parseString(s: String) -> NSMutableAttributedString{
        let t = replaceString(t: s);
        //print("final string - \(t.0)")
        let out = NSMutableAttributedString(string: t.0);
        for i in t.1{
        //    print("final ranges - \(i.0.0) to \(i.0.1) : \(out.length)")
            out.setAttributes([.link: i.1], range: NSRange(location: i.0.0, length: i.0.1-i.0.0+1));
        }
        return out;
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        bookmarkButton.articleCompleteData = articleContent ?? articleData();
        
        setBookmarkColor();
        
        articleCatagoryLabel.text = articleContent?.articleCatagory ?? "NO Cata.";
        articleCatagoryLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight, .topLeft, .topRight], radius: 5);
        
       // mainScrollView.topAnchor.constraint(equalToSystemSpacingBelow: view.topAnchor, multiplier: 1).isActive = true;
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;

        
        
        articleText.attributedText = parseString(s: articleContent?.articleBody ?? "");
        articleText.font = UIFont(name: articleText.font!.fontName, size: CGFloat(fontSize));
        articleText.sizeToFit();
        articleText.isScrollEnabled = false;
        articleText.tintColor = UIColor.systemBlue;
       // articleText.translatesAutoresizingMaskIntoConstraints = true;
        
        
        articleTitle.text = "Loading images..."; // see func viewdidappear
        articleDate.text = epochClass.epochToFormatedDateString(epoch: articleContent?.articleUnixEpoch ?? -1); // TODO: IMPLEMENT A FUNC TO GET INT TO STRING DATE
        articleAuthor.text = "By " + (articleContent?.articleAuthor ?? " No Author");

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
            buttonImage.isSelected = false;
            
            buttonImage.addTarget(self, action: #selector(toggleZoom), for: .touchUpInside);
            
            //print("\(imageView.image != nil)" + " - " + (articleContent?.articleImages?[imageIndex] ?? ""))
           // imageZoom.addSubview(imageView);
            //imageZoom.delegate = self;
            
            self.imageScrollView.addSubview(buttonImage);
        }
        imageScrollView.contentSize = CGSize(width: (imageFrame.size.width * CGFloat(imageSize)), height: imageScrollView.frame.size.height);
        imageScrollView.delegate = self;
        imageScrollView.layer.cornerRadius = 10;
        
        articleTitle.text = articleContent?.articleTitle; // set article title herer
        articleTitle.font = UIFont(name: articleTitle.font.fontName, size: CGFloat(fontSize));
    }

    
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        imagePageControl.currentPage = Int(imageScrollView.contentOffset.x / imageFrame.size.width);
    }
}
