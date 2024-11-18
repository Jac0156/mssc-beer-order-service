package guru.sfg.beer.order.service.sm;

import java.util.Optional;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderStateChangeInterceptor extends 
        StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum>{
            
    private final BeerOrderRepository beerOrderRepository;

    @Transactional
    @Override
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state,
            Message<BeerOrderEventEnum> message, 
            Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
            StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine,
            StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> rootStateMachine) {

        log.debug("Pre-State Change");
        
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(
                    (String) (msg.getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ID_HEADER, " ")))
                    .ifPresent(orderId -> {

                        log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());

                        BeerOrder beerOrder = beerOrderRepository.getReferenceById(UUID.fromString(orderId));
                        beerOrder.setOrderStatus(state.getId());
                        beerOrderRepository.saveAndFlush(beerOrder);
                    });
        });

    }

    
}
