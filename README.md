# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1단계 - 경로 조회 타입 추

### 요구사항
- [x] 노선 추가 시 소요시간 정보 추가
- [x] 구간 추가 시 소요시간 정보도 추가
- [x] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 수정

### 작업목록
- [x] 노선 추가 인수테스트 소요시간 추가
- [x] 구간 생성 유닛테스트 추가
- [x] 구간 생성 유닛테스트에 따라 소요시간관련 기능 수정
- [x] 노선 생성 유닛테스트 추가
- [x] 노선 생성 유닛테스트에 따라 소요시간관련 기능 수정
- [x] 소요시간 not null 에 따라 발생하는 오류 수정
- [x] 구간 추가 유닛테스트 소요시간 추가
- [x] 구간 추가 유닛테스트에 따라 소요시간관련 기능 수정
- [x] 경로 조회 소요시간 추가
- [x] 경로 조회시 최소 시간 기준으로 조회 수정
- [x] 중복 코드 리팩터링

## 2단계 - 요금 조회

### 요구사항
- [x] 경로 조회 결과에 요금 정보를 포함

### 작업목록
- [x] Cucumber 컨밴션 통일
- [x] ShortestPath 에서 Station 정보 추출
- [x] ShortestPathFactory 존재 의미 고민
- [x] 경로 조회 결과에 요금 정보 추가

## 3단계 - 요금 정책 추가

### 요구사항
- [x] 노선별 추가 요금
    - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    - 경로 중 추가 요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
- [x] 로그인 사용자의 경우 연령별 요금으로 계산
    - 청소년(13세 이상 ~ 19세 미만): 운임에서 350원을 공제한 금액의 20%할인
    - 어린이(6세 이상 ~ 13세 미만): 운임에서 350원을 공제한 금액의 50%할인

### 작업목록
- [x] 노선 생성 시 추가 요금 정보 추가
- [x] 노선 수정 시 추가 요금 정보 추가
- [x] 경로 조회 시 사용 노선 추가 요금 적용
- [x] 경로 조회 시 사용 노선 추가 요금 중 가장 높은 추가 요금만 적용
- [x] 경로 조회 시 청소년, 어린이 350원 공제 적용
- [x] 경로 조회 시 청소년, 어린이 공제한 금액의 20%, 50% 할인 적용
- [x] 경로의 정보 거리, 시간, 요금을 한번에 조회할 수 있도록 리팩터링 필요
