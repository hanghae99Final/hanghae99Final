# MyTaek1 - 라이브 커머스 서비스

![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/b5dc4a4d-01aa-4dac-aba5-ad2a67a46d76)
<aside>
🎬 방송 송출 기술과 실시간 재고 관리, 효율적인 결제 시스템을 결합한 다중 서비스 플랫폼

- **실시간 스트리밍 기능을 제공하는 라이브 커머스 서비스**
- **동시성 제어를 통한 안정적인 결제 서비스**
</aside>

### 🔗 URL
[마이택일](https://mytaek1.store)

### 🗒️ Notion
[마이택일 노션](https://cut-radiator-838.notion.site/6-e9d48583e62a40d28263e5276a1b745a?pvs=4)


### 📐Architecture
![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/ef6c2378-1257-487b-84a5-f7eb55a3221e)

### 📈ERD
![image](https://github.com/hanghae99Final/mytaek1-service/assets/115725752/8ddf4209-cfdd-4df7-8484-0db5beccbabd)

### 💻기술스택
  <br /><br />
- ### SERVICE SERVER
  <img src="https://img.shields.io/badge/REDIS-DC382D?style=for-the-badge&logo=Redis&logoColor=white"><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"><br />  
- ### MEDIA SERVER
  <img src="https://img.shields.io/badge/FFmpeg-007808?style=for-the-badge&logo=FFmpeg&logoColor=white"><img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white"><img src="https://img.shields.io/badge/NGINX RTMP-02303A?style=for-the-badge&logoColor=white"><br />
- ### DEVOPS
  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"><img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"><img src="https://img.shields.io/badge/Code%20Deploy-2F93E0?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/Github Actions-2088FF?style=for-the-badge&logo=Github Actions&logoColor=white"><img src="https://img.shields.io/badge/AWS%20Lambda-FF9900?style=for-the-badge&logo=AWS%20Lambda&logoColor=white">  <br />
- ### TESTING TOOLS
  <img src="https://img.shields.io/badge/Apache JMeter-D22128?style=for-the-badge&logo=Apache JMeter&logoColor=white">
  <br />
  <br />
  <br />
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
<summary> jmeter / k6</summary>
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
<summary> RDBMS / NoSQL</summary>
<div markdown="1">

```
실시간으로 생성 쿼리가 많이 발생하는 채팅 서버에 적합한 DB는 NoSQL이고,
많은 쿼리가 발생하지 않는 서비스 서버에 적합한 DB는 RDBMS라고 판단했지만,
SOA를 준수하는 차원에서 하나의 DB를 사용하기로 결정하였습니다.
따라서 NoSQL, RDBMS 둘 중 하나를 택해야 했는데,
분리되어 있는 서비스에서 중요한 것은 데이터의 일관성이라 결론을 내렸습니다.
무결성을 보장하는 MySQL DB를 선택하여 서비스들이
일관성 있는 데이터를 공유하도록 하였습니다.
비동기 프로그래밍 방식을 택한 프로젝트에서
MySQL이 동기 블로킹 방식으로 동작한다는 점은 치명적이었지만,
비동기 Non-Blocking 방식으로 I/O 할 수 있도록 R2DBC드라이버를 사용하여 극복하였습니다.
```

</div>
</details>

### 👨‍👩‍👦‍👦 팀원 소개
<table>
  <tr>
    <td align="center"><b><a href="https://github.com/KimDabomi">🦙김다보미</a></b></td>
    <td align="center"><b><a href="https://github.com/tls3254">김대일</a></b></td>
    <td align="center"><b><a href="https://github.com/Crescent0kt">오경택</a></b></td>
    <td align="center"><b><a href="https://github.com/SeungbaeLee">이승배</a></b></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/KimDabomi"><img src="" width="100px" /></a></td>
    <td align="center"><a href="https://github.com/tls325"><img src="" width="100px" /></a></td>
    <td align="center"><a href="https://github.com/Crescent0kt"><img src="" width="100px" /></a></td>
    <td align="center"><a href="https://github.com/SeungbaeLee"><img src="" width="100px" /></a></td>
  </tr>
  <tr>
    <td align="center"><b>🌱 Spring</b></td>
    <td align="center"><b>🌱 Spring</b></td>
    <td align="center"><b>🌱 Spring</b></td>
    <td align="center"><b>🌱 Spring</b></td>
  </tr>
</table>


 
 
