package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
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

        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.
        try{
            if(parkingLotRepository3.findById(parkingLotId).isPresent() || userRepository3.findById(userId).isPresent()){
                throw new Exception("Cannot make reservation");

            }
            ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();
            User user=userRepository3.findById(userId).get();

//       Fetching Spots-List from ParkingLots
            List<Spot> spots=parkingLot.getSpotList();
            double minprice=Double.MAX_VALUE;
            Spot spotReserve=null;

            for(Spot spot:spots){
                if(!spot.getOccupied() && numberOfWheels==2 && spot.getSpotType()==SpotType.TWO_WHEELER && spot.getPricePerHour()<minprice){
                    minprice=spot.getPricePerHour();
                    spotReserve=spot;
                    spot.setOccupied(true);
                    break;
                } else if (!spot.getOccupied() && numberOfWheels<=4 && spot.getSpotType()==SpotType.FOUR_WHEELER && spot.getPricePerHour()<minprice) {
                    spotReserve=spot;
                    minprice= spot.getPricePerHour();
                    spot.setOccupied(true);
                    break;
                } else if (!spot.getOccupied() && numberOfWheels!=2 && numberOfWheels!=4 && spot.getSpotType()==SpotType.OTHERS && spot.getPricePerHour()<minprice) {
                    spotReserve=spot;
                    minprice=spot.getPricePerHour();
                    spot.setOccupied(true);
                    break;
                }
            }

            if(spotReserve==null){
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation=new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setUser(user);
            reservation.setSpot(spotReserve);

            user.getReservationList().add(reservation);
            spotReserve.getReservationList().add(reservation);

            spotRepository3.save(spotReserve);
            userRepository3.save(user);
//        reservationRepository3.save(reservation);
            return reservation;


        }catch (Exception e){
            return null;
        }
    }
}