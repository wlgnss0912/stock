package com.example.stock.servivce;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // @Transactional
    // synchronized를 사용해도 Transactional은 StockService를 필드로 가지는 class를 새로 만들어 수행
    // synchronized는 각 프로세스 안에서만 보장이된다.
    // 서버가 여러개라면, 동시에 데이터에 접근이 가능하다.(실무에서는 여러대의 서버를 보통 사용한다.)
    // 그러면 여러 쓰레드에서 동시에 데이터에 접근이 가능하고 문제가 발생하게 된다.
    public synchronized void decrease(Long id, Long quantity) { //synchronized : 해당 메서드는 한개의 쓰레드만 접근 가능
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
