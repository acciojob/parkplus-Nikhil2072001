package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
User user = userRepository3.findById(userId).get();
ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
List<Spot> spotList = new ArrayList<>();
int c=Integer.MAX_VALUE;
int wheels=0;
        Reservation reservation = new Reservation();
Spot spot =null;
for(Spot s: spotList){
    if(!s.getOccupied() && c>s.getPricePerHour() ){
        if(numberOfWheels==2)
        spot=s;
        else if(numberOfWheels==4 && s.getSpotType().equals("FOUR_WHEELER") ||  s.getSpotType().equals("OTHERS"))
            spot=s;
        else
            spot =s;
    }
}

reservation.setSpot(spot);
reservation.setUser(user);
reservation.setNumberOfHours(timeInHours);
if(spot==null)
    throw new Exception("Cannot make reservation");
reservationRepository3.save(reservation);
return reservation;
    }
}
