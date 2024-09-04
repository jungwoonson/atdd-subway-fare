Feature: 경로 조회 기능
  Scenario: 최단거리 경로를 조회한다.
    Given 지하철역이 등록되어 있다
    And 지하철 노선이 등록되어 있다
    When 출발역과 도착역을 입력하여 최단거리 경로를 조회하면
    Then 출발역에서 도착역까지 최단거리의 경로가 조회된다
    And 출발역에서 도착역까지 최단거리가 조회된다
