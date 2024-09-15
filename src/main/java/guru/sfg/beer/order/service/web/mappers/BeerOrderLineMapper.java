package guru.sfg.beer.order.service.web.mappers;

import org.mapstruct.Mapper;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;

@Mapper(uses = {DateMapper.class})
public interface BeerOrderLineMapper { 
         
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
