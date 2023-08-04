package com.example.stock.servivce;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)//spring에서 트랜잭션을 부모와 따로 분리하고 싶을때 사용
    public void decrease(Long id, Long quantity) { //synchronized : 해당 메서드는 한개의 쓰레드만 접근 가능
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}

// synchronized를 사용해도 Transactional은 StockService를 필드로 가지는 class를 새로 만들어 수행
// synchronized는 각 프로세스 안에서만 보장이된다.
// 서버가 여러개라면, 동시에 데이터에 접근이 가능하다.(실무에서는 여러대의 서버를 보통 사용한다.)
// 그러면 여러 쓰레드에서 동시에 데이터에 접근이 가능하고 문제가 발생하게 된다.

/*
* 부모의 트랜잭션과 동일한 범위로 묶인다면 Synchronized 와 같은 문제가 발생합니다.
* Database 에 commit 되기전에 락이 풀리는 현상이 발생합니다.
* 그렇기때문에 별도의 트랜잭션으로 분리를 해주어 Database 에 정상적으로 commit 이 된 이후에 락을 해제하는것을 의도하였습니다.
* 핵심은 lock 을 해제하기전에 Database 에 commit 이 되도록 하는것입니다.
*/

