# MyTaek1 - 라이브 커머스 서비스

![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/b5dc4a4d-01aa-4dac-aba5-ad2a67a46d76)
<aside>
🎬 방송 송출 기술과 실시간 재고 관리, 효율적인 결제 시스템을 결합한 다중 서비스 플랫폼

- **실시간 스트리밍 기능을 제공하는 라이브 커머스 서비스**
- **동시성 제어를 통한 안정적인 결제 서비스**
</aside>

### 👨‍👩‍👦‍👦 팀원 소개
<table>
  <tr>
    <td align="center"><b><a href="https://github.com/KimDabomi">🦙김다보미</a></b></td>
    <td align="center"><b><a href="https://github.com/tls3254">김대일</a></b></td>
    <td align="center"><b><a href="https://github.com/Crescent0kt">오경택</a></b></td>
    <td align="center"><b><a href="https://github.com/SeungbaeLee">이승배</a></b></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/KimDabomi"><img src="https://github.com/hanghae99Final/mytaek1-service/assets/115725752/706dcc7a-2997-4533-9db3-77ac67cd74c2" width="100px" /></a></td>
    <td align="center"><a href="https://github.com/tls325"><img src="https://github.com/hanghae99Final/mytaek1-service/assets/115725752/62c1a1a8-0802-4770-a61d-f0f3ba01d233" width="100px" /></a></td>
    <td align="center"><a href="https://github.com/Crescent0kt"><img src="https://github.com/hanghae99Final/mytaek1-service/assets/115725752/76819fad-5343-48c3-97e2-49841540b6f1" width="100px" /></a></td>
    <td align="center"><a href="https://github.com/SeungbaeLee"><img src="https://github.com/hanghae99Final/mytaek1-service/assets/115725752/8d8bdc79-c1ee-4043-a157-f5a63efcf46f" width="100px" /></a></td>
  </tr>
  <tr>
    <td align="center"><b>🌱 Spring</b></td>
    <td align="center"><b>🌱 Spring</b></td>
    <td align="center"><b>🌱 Spring</b></td>
    <td align="center"><b>🌱 Spring</b></td>
  </tr>
</table>

### 🔗 URL
[마이택일](https://mytaek1.store)
[마이택일 노션](https://cut-radiator-838.notion.site/6-e9d48583e62a40d28263e5276a1b745a?pvs=4)


### 📐Architecture
![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/d9f8df8c-08f0-4ccd-a628-810bed7d88e4)



### 💻기술스택
- ### SERVICE SERVER
  <img src="https://img.shields.io/badge/REDIS-DC382D?style=for-the-badge&logo=Redis&logoColor=white"><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"><br />  
- ### MEDIA SERVER
  <img src="https://img.shields.io/badge/FFmpeg-007808?style=for-the-badge&logo=FFmpeg&logoColor=white"><img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white"><img src="https://img.shields.io/badge/NGINX RTMP-02303A?style=for-the-badge&logoColor=white"><br />
- ### DEVOPS
  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"><img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"><img src="https://img.shields.io/badge/Code%20Deploy-2F93E0?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/Github Actions-2088FF?style=for-the-badge&logo=Github Actions&logoColor=white"><img src="https://img.shields.io/badge/AWS%20Lambda-FF9900?style=for-the-badge&logo=AWS%20Lambda&logoColor=white">  <br />
- ### TESTING TOOLS
  <img src="https://img.shields.io/badge/Apache JMeter-D22128?style=for-the-badge&logo=Apache JMeter&logoColor=white">
  <br />
  
### 주요 기능

👨🏻‍🔧 **방송 추가 기능 - 이미지 업로드, 상품 정보도 포함**

- 방송 추가 시 이미지 업로드 후, 썸네일 생성해서 리스트에 표시
![Animation](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/9c3a90ce-9a63-4b32-a85d-9b27a4113e13)

👨🏻‍🔧 **회원가입을 통해 발급 받은 스트림키로만 방송 시작 가능**

- 서버 디비에 저장된 스트림키를 인식
- 저장된 스트림키가 아니라면 미디어 서버에 접근 불가
![Animation (1)](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/ea23c067-9abf-4049-98a8-64d711d7cd0e)


🕰️ **짧은 딜레이를 통해 실시간 방송 시청 가능**

- 약 4~5초의 짧은 딜레이 시간
- 방송 화질 선택 가능
![Animation (2)](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/660283b6-fa62-405c-ac76-e0c912189a2d)


💼 **동시성 제어를 통한 실시간 재고 업데이트 기능으로 정확한 재고 정보**

- 주문하기를 누르면 바로 재고가 반영됨
![Animation (3)](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/b6a38826-a82e-4da5-b52c-5d77a5cd1b4e)


💰 **효율적인 결제 시스템**

- 모듈 결제하기
- 카드정보 입력으로 바로 결제 요청이 완료되고, 결제는 비동기로 처리
![Animation (4)](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/757dd0dd-3f0b-43de-855c-34ec0a2c4087)


### 트러블 슈팅
<details>
<summary> 락이 적용되지만, 재고차감이 정상적으로 되지 않음 </summary>
<div markdown="1">

```
원인 : 동시성 제어가 필요한 재고를 컨트롤러에서 미리 찾아버려서, 서비스 메서드에서 1차 캐싱된 재고를 가져와서 의도한 대로 동시성 제어가 되지 않음

해결 : 컨트롤러에서 재고를 조회하는 부분을 삭제하여 락을 가진 상태에서 재고 조회와 재고 차감이 연속적으로 이루어지므로 동시성 문제를 효과적으로 해결
```
 ![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/da54855f-7638-4751-b5fa-221d5de69e4d)

</div>
</details>

<details>
<summary> 방송 시청 중 멈춤 </summary>
<div markdown="1">

```
원인 : 기존 hls_fragment의 값이 600ms로 설정되어있어 받아와야 될 파일을 삭제함

해결 : hls_fragment 값을 600ms → 3s로 수정 후 정상 작동 확인
```
![Untitled](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/218b7d49-e44f-4797-b43c-908079cb40fd)

```
```

</div>
</details>

<details>
<summary> HTTPS로 연결된 서비스 서버에 미디어 서버가 HTTP 요청으로 들어와서 접근이 제한됨 </summary>
<div markdown="1">

```
원인 : 서비스 서버에는 HTTPS 적용, 미디어 서버는 HTTPS 미적용

해결 : 스트리밍 서버 도메인에 SSL 설정해서 해결
```
![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/d99f848e-9153-40ad-af3e-6114bc5a8f30)

</div>
</details>

### ❓ 기술적 의사결정
<details>
<summary> 동시성 제어 : Redis Distributed Lock/Redis Fair Lock/Redis Sorted Set</summary>
<div markdown="1">

```
• Redis Distributed Lock
- 직관적으로 코드를 이해하고 구현하기 용이함
- AOP 기반의 코드 레퍼런스가 있어 사용하기 용이함

• Fair Lock  
-  순서를 보장한다는 장점을 가졌지만, 대규모 요청에는 추가 오버헤드로 인해서 성능이 떨어짐
• Sorted Set 
- 대기열이 길어질수록 대기 시간이 길어지므로 성능 저하 가능성 有, 우선 순위가 잘못 설정되면 요청이 무시됨

→ 레퍼런스가 다양하고, 기본적으로 분산락 기능을 제공해주는 Redisson 분산락 선택
```

</div>
</details>

<details>
<summary> CI/CD 무중단 배포 : CodeDeploy+S3+GithubAction / Docker-compose+GithubAction </summary>
<div markdown="1">

```
• Github Actions + CodeDeploy + S3 + EC2
- 블루/그린 배포는 하나의 버전만 프로덕션 되기 때문에 버전 관리 문제를 방지할 수 있음
- 운영 환경에 영향을 주지 않고 실제 서비스 환경으로 새 버전 테스트가 가능
- 새 버전으로 전환 후에 문제가 생겼을 시에 구 버전으로 되돌리기 위한 롤백이 용이함
- 오토스케일링과 결합하여 무중단 배포 가능

• Github Actions + Docker-compose + EC2 
- 도커를 이용해서 배포 시 빠른 배포와 환경 일관성을 유지하며 배포할 수 있음
- 복잡한 설정과 관리를 필요로해, 복잡한 애플리케이션의 경우 컨테이너 간의 네트워킹 및 데이터 공유 문제가 발생할 수도 있음
```
</div>
</details>

<details>
<summary> 테스트 툴 : jmeter / k6</summary>
<div markdown="1">

```
• Jmeter
- 대용량 트래픽을 시뮬레이션 할 수 있음
- 플러그인 설치 시 RTMP와 같은 방송 송출 및 시청 테스트가 가능함
- GUI가 존재해 사용이 편리함
- 100% 자바로 개발된 오픈소스 성능 테스트 도구
- 다양한 통신 프로토콜을 지원함
- 분산 부하 테스트 기능을 기본적으로 제공해줌

• K6 - 익숙치 않은 파이썬과 CLI을 사용함
- Python을 이용하여 스크립트를 작성함
- 로컬 혹은 원격지의 스크립트를 로드하여 테스트 할 수 있음
- GUI 가 없고, CLI툴을 사용하여 성능 테스트를 수행
```

</div>
</details>

<details>
<summary> 대용량 트래픽 처리 </summary>
<div markdown="1">

```
• Elastic Load Balancer
- 트래픽을 분산하고 고가용성을 제공
- 도메인 관리 및 DNS 서비스로서, 로드 밸런서의 도메인을 관리
- 외부 도메인과 연결, ssl 설정이 용이함
• Auto Scaling Group
- 트래픽 증가에 따라 자동으로 리소스를 조절해줘서 편리함

→ 오토스케일링을 통해서 인스턴스를 추가 생성 후 + 요청을 분산 
→ 다른 포트로 들어오는 요청 8080 포트로 실행
```

</div>
</details>


### 📈ERD
![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/8ddf4209-cfdd-4df7-8484-0db5beccbabd)




 
 
