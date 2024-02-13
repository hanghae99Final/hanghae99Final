package org.sparta.mytaek1.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.time.LocalDateTime;

/**
 * @DistributedLock 선언 시 수행되는 Aop class
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    //@DistributedLock 어노테이션이 붙은 메소드를 대상으로 하는 Aspect를 정의
    @Around("@annotation(org.sparta.mytaek1.global.redis.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {//조인 포인트는 프로그램 실행 중에 다른 코드와 결합할 수 있는 특정 지점을 의미

        // 메소드 서명과 어노테이션 정보를 획득
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // 분산 락 키를 생성
        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);
        log.info("rLock : "+ rLock + " //락키 생성 체크 : "+ key + " //시간 : " + LocalDateTime.now());

        try {
            // 락을 획득하기 위해 시도
            // 정의된 waitTime까지 획득을 시도한다, 정의된 leaseTime이 지나면 잠금을 해제한다.
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            log.info(key + "가 락 획득 시도" + LocalDateTime.now());
            if (!available) {
                return false;
            }

            // AOP를 통해 DistributedLock 어노테이션이 선언된 메서드를 별도의 트랜잭션으로 실행한다.
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            // 락을 해제
            rLock.unlock();
            log.info(key + "가 획득한 락 해제" + LocalDateTime.now());
        }
    }
}
