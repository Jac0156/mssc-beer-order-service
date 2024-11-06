package guru.sfg.beer.order.service.services;

import java.util.UUID;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.sm.BeerOrderStateChangeInterceptor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager{

    public final static String BEER_ID_HEADER = "beer_id"; 

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderStateChangeInterceptor beerOrderStateChangeInterceptor;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {

        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        sendBeerOrderEvent(savedBeerOrder, BeerOrderEventEnum.VALIDATE_ORDER);
        return savedBeerOrder;
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum eventEnum) {

        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = build(beerOrder);

        sm.sendEvent(Mono.just(
                MessageBuilder
                    .withPayload(eventEnum)
                    .setHeader(BEER_ID_HEADER, beerOrder.getId().toString())
                    .build()))
                .subscribe();
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder){
        
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());

        sm.stopReactively().block();
        sm.getStateMachineAccessor()
        .doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(beerOrderStateChangeInterceptor);
            sma.resetStateMachineReactively(new
            DefaultStateMachineContext<BeerOrderStatusEnum,BeerOrderEventEnum>(beerOrder.getOrderStatus(), null, null, null)).block();
        });
        sm.startReactively().block();

        return sm;
    }

    @Override
    public void processValidationResult(UUID beerOrderId, Boolean isValid) {

        BeerOrder beerOrder = beerOrderRepository.getReferenceById(beerOrderId);

        if(isValid) {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_PASSED);
            BeerOrder validatedOrder = beerOrderRepository.findById(beerOrderId).get();
            sendBeerOrderEvent(validatedOrder, BeerOrderEventEnum.ALLOCATE_ORDER);
        } else {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_FAILED);
        }
    }

}
