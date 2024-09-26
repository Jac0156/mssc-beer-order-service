package guru.sfg.beer.order.service.web.mappers;

import javax.annotation.processing.Generated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T21:56:16-0400",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
@Primary
public class BeerOrderLineMapperImpl extends BeerOrderLineMapperDecorator {

    @Autowired
    @Qualifier("delegate")
    private BeerOrderLineMapper delegate;

    @Override
    public BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto)  {
        return delegate.dtoToBeerOrderLine( dto );
    }
}
