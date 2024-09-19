Feature: 경로 조회 기능

  Scenario: 최단거리 경로를 조회한다
    Given 지하철역이 등록돼 있다
    And 거리 계산을 위한 지하철 노선이 등록돼 있다
    When 출발역과 도착역을 입력하여 최단거리 경로를 조회하면
    Then 출발역에서 도착역까지 최단거리의 경로가 조회된다
    And 출발역에서 도착역까지 최단거리와 소요시간을 함께 응답한다

  Scenario: 두 역의 최소 시간 경로를 조회한다
    Given 지하철역이 등록돼 있다
    And 시간 계산을 위한 지하철 노선이 등록돼 있다
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
    Then 최소 시간 기준 경로를 응답한다
    And 총 거리와 소요 시간을 함께 응답한다
