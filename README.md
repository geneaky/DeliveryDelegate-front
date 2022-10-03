# 달대표 | DeliveryDelegate   


![image](https://user-images.githubusercontent.com/50348197/166867011-c2e0d157-c44a-494a-be98-eed7be41556a.png)
  
**2022.03.02 ~ 2022.**

컴퓨터소프트웨어공학과 4학년 졸업작품 `ANDROID`



# Introduction
## 목적
📌 간단한 게임을 통한 배달비 절약  
📌 동네 맛집 리뷰로 맛집 홍보 및 동네 상권 살리기

## 작품의 구성 및 동작 설명
![image](https://user-images.githubusercontent.com/50348197/193555157-ccbfcfd3-9bb5-4585-a241-a1dd0c51f256.png)

* **(동네 주민 배달 대행)** 배달음식이 먹고 싶은데 배달비가 아까울 때, 『달대표』를 실행시켜 게임방에 참여한 동네 주민끼리 간단한 게임을 진행하고 거기서 뽑힌 배달 대표가 가게를 돌며 사용자의 주문을 수령해 미리 지정한 랜드마크로 가져온다.   
* **(영수증 리뷰)** 우리 동네 진짜 맛집을 알고 싶을 때 『달대표』를 실행시켜 사용자가 직접 방문한 영수증을 사진 찍으면, 영수증에 해당하는 가게를 문자 인식한다. 직접 방문한 가게만 리뷰를 작성할 수 있기 때문에 리뷰의 신뢰도가 올라간다. 

## ERD
https://www.erdcloud.com/d/hyRv9XbeEQ3WyTNv6

## Made With
**Anroid**
 `Android Studio` `Kotlin`
 
**Library**
 `okhttp` `retrofit`  ...  and so on,.......
 


  
# 주요 기능
![image](https://user-images.githubusercontent.com/50348197/193554204-6ccd4514-5792-4dc7-9b33-dbcaa651934f.png)


### **✅ 동네 주민 배달 대행**
1. 주민등록상 거주지 및 위치 기반으로 같은 동네 주민 인증
2. 배달 대표(이하 달대표) 를 정하는 미니게임 수행
3. 수령한 음식을 나눠줄 랜드마크 설정 후 달대표가 음식 수령 및 배부
   
   
![image](https://user-images.githubusercontent.com/50348197/193554630-e56aea4f-79de-40be-8c38-03db9e27f471.png)  
### **✅ 영수증 리뷰** 
1. OCR기술을 활용한 영수증 문자 인식  
2. 위치와 영수증 내용을 동시다발적으로 확인  
3. 기존 리뷰에 타 사용자가 좋아요 표시 가능  
4. 누적 리뷰 10회마다 배달 대표 면제권 제공  


## 개발자
👩‍💻 [조윤아](https://github.com/whdbsdk1115)   
👩‍💻 [문채영](https://github.com/Chae0-99)


