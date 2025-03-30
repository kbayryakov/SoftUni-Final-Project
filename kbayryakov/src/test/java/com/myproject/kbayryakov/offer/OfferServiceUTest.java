package com.myproject.kbayryakov.offer;

import com.myproject.kbayryakov.models.Offer;
import com.myproject.kbayryakov.repositories.OfferRepository;
import com.myproject.kbayryakov.services.OfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferServiceUTest {
    @InjectMocks
    private OfferService offerService;

    @Mock
    private OfferRepository offerRepository;

    @Test
    public void testFindAllOffersByUsername() {
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        List<Offer> offerList = List.of(offer1, offer2);
        String username = "Ivan";
        when(offerRepository.findAllByUser_Username(username)).thenReturn(offerList);

        List<Offer> result = offerService.findAllOffersByUsername(username);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(offer1.getDescription(), result.get(0).getDescription());
        assertEquals(offer2.getDescription(), result.get(1).getDescription());
        verify(offerRepository, times(1)).findAllByUser_Username(username);
    }

    @Test
    public void testFindAll() {
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        List<Offer> offerList = List.of(offer1, offer2);
        when(offerRepository.findAll()).thenReturn(offerList);

        List<Offer> result = offerService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(offerRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        List<Offer> offerList = List.of(offer1, offer2);
        UUID id = UUID.randomUUID();
        when(offerRepository.findById(id)).thenReturn(Optional.of(offer1));

        Offer result = offerService.findById(id);

        assertNotNull(result);
        assertEquals(offer1.getId(), result.getId());
        verify(offerRepository, times(1)).findById(id);
    }
}
