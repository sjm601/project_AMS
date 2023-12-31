![header](https://capsule-render.vercel.app/api?type=waving&height=150&text=Project_AMS&fontAlign=60&fontAlignY=50&color=gradient)

## 프로젝트 개요
* 계좌 관리 어플리케이션(Account Manager Application)

---
## 프로젝트 기능
* 신규 계좌 등록
* 계좌 조회
* 기존 계좌 삭제

---
## 제작 및 실행 환경
* JDK : Java SE 11
* IDE : Eclipse 및 IntelliJ IDEA

---
## 사용 API
* AWT API

---
## 실행법
* 소스폴더(src 폴더) 내의 ams.app 패키지 내의 AMS.java 실행

---
## 실행 시 주의사항
* 이클립스 : 프로젝트 실행환경 -> VM arguments 에 -Dfile.encoding=MS949 적용
* 인텔리제이 : 프로젝트 실행 구성 -> 어플리케이션 구성 추가 -> 실행 옵션에서 VM 옵션 추가 -> VM 옵션 란에 -Dfile.encoding=MS949 적용

---
## 소스 내 패키지
* ams.app : 실행을 위한 메인 클래스
* ams.domain : 프로그램 주요 기능
* ams.exception : 프로그램 작동시 예외 처리용 클래스\
* ams.view : GUI 환경 구현을 위한 AWT API를 이용한 Frame 구현 클래스 패키지
* ams.util : GUI 환경의 원활한 실행을 위한 유효성 검사 클래스 및 Enum 클래스 패키지

---
## 기능 및 설명
* 원하는 계좌 종류 선택
  * 입출금계좌 / 마이너스계좌
* 실행 하고자 하는 기능 종류 선택 (계좌 종류 선택 후에 기능 선택 가능)
  * 등록 / 조회 / 검색 / 삭제
* 등록
  * 신규 계좌 생성
    * 예금주 명, 비밀번호, 금액 정보 입력 후 신규등록 버튼 클릭
* 조회
  * 계좌번호로 계좌 조회
    * 조회하고자 하는 계좌의 계좌번호 입력 후 조회 버튼 클릭
* 검색
  * 예금주명으로 계좌 검색
    * 검색하고자 하는 계좌의 예금주명 입력 후 검색 버튼 클릭
* 삭제
  * 기존 계좌 삭제
    * 삭제하고자 하는 계좌번호, 비밀번호 입력 후 삭제 버튼 클릭
* 계좌 전체 조회
  * 전체 계좌 목록 출력
    * 전체 조회 버튼 클릭

---
## 업데이트 내역

### v3
* 실행 환경 변경 (CLI 에서 AWT API를 이용한 GUI 환경으로 변경)
* 원활한 GUI 환경 사용을 위해 유효성 검증 클래스 및 Enum 클래스 추가

### v2
* Array 구조를 Collection 구조로 변경 (List 사용)

### v1
* CLI(Command Line Interface), 콘솔 환경에서만 실행 가능
* 컴퓨터 메모리 상에서만 일시적으로 정보를 생성 및 저장, 수정, 출력, 삭제가 가능
* 단순 배열(Array) 구조만 사용하여 제작
* 업데이트 및 프로그램 확장성을 위해 Interface 사용
