package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.brewery.model.BeerOrderLineDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
@Qualifier("delegate")
public class BeerOrderLineMapperImpl_ implements BeerOrderLineMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        if ( line == null ) {
            return null;
        }

        BeerOrderLineDto.BeerOrderLineDtoBuilder beerOrderLineDto = BeerOrderLineDto.builder();

        beerOrderLineDto.beerId( line.getBeerId() );
        beerOrderLineDto.createdDate( dateMapper.asOffsetDateTime( line.getCreatedDate() ) );
        beerOrderLineDto.id( line.getId() );
        beerOrderLineDto.lastModifiedDate( dateMapper.asOffsetDateTime( line.getLastModifiedDate() ) );
        beerOrderLineDto.orderQuantity( line.getOrderQuantity() );
        beerOrderLineDto.quantityAllocated( line.getQuantityAllocated() );
        beerOrderLineDto.upc( line.getUpc() );
        if ( line.getVersion() != null ) {
            beerOrderLineDto.version( line.getVersion().intValue() );
        }

        return beerOrderLineDto.build();
    }

    @Override
    public BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto) {
        if ( dto == null ) {
            return null;
        }

        BeerOrderLine.BeerOrderLineBuilder beerOrderLine = BeerOrderLine.builder();

        beerOrderLine.beerId( dto.getBeerId() );
        beerOrderLine.createdDate( dateMapper.asTimestamp( dto.getCreatedDate() ) );
        beerOrderLine.id( dto.getId() );
        beerOrderLine.lastModifiedDate( dateMapper.asTimestamp( dto.getLastModifiedDate() ) );
        beerOrderLine.orderQuantity( dto.getOrderQuantity() );
        beerOrderLine.quantityAllocated( dto.getQuantityAllocated() );
        beerOrderLine.upc( dto.getUpc() );
        if ( dto.getVersion() != null ) {
            beerOrderLine.version( dto.getVersion().longValue() );
        }

        return beerOrderLine.build();
    }
}
