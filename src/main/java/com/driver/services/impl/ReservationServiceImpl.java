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
        try{
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            User user = userRepository3.findById(userId).get();

            if(parkingLot==null || user==null){
                throw new Exception("Cannot make reservation");
            }

            List<Spot> spotList = parkingLot.getSpotList();

            Integer minBill = Integer.MAX_VALUE;

            Spot reqspot=null;
            for(Spot spot:spotList){
                if(spotTypeFunction(spot.getSpotType())>=numberOfWheels && spot.getOccupied()==false && spot.getPricePerHour()*timeInHours<minBill){
                    minBill=spot.getPricePerHour()*timeInHours;
                    reqspot=spot;
                }
            }
            if (reqspot==null){
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation = new Reservation(timeInHours);
            reservation.setUser(user);
            List<Reservation> reservationList = user.getReservationList();
            if(reservationList==null){
                reservationList = new ArrayList<>();
            }
            reservationList.add(reservation);
            user.setReservationList(reservationList);


            reservation.setSpot(reqspot);
            List<Reservation> reservationList1 = reqspot.getReservationList();
            if(reservationList1==null){
                reservationList1 = new ArrayList<>();
            }
            reservationList1.add(reservation);
            reqspot.setReservationList(reservationList1);
            reqspot.setOccupied(true);

            userRepository3.save(user);
            spotRepository3.save(reqspot);

            return reservation;

        }catch (Exception e){
            return null;
        }

        //User user = userRepository3.findById(userId).get();
//ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
//List<Spot> spotList = new ArrayList<>();
//int c=Integer.MAX_VALUE;
//spotList=parkingLot.getSpotList();
//        Reservation reservation = new Reservation();
//Spot spot =null;
//for(Spot s: spotList){
//    if(!s.getOccupied() && c>s.getPricePerHour()  ){
//        if(numberOfWheels==2)
//        spot=s;
//        else if(numberOfWheels==4 && s.getSpotType().equals("FOUR_WHEELER") ||  s.getSpotType().equals("OTHERS"))
//            spot=s;
//        else if(numberOfWheels>4)
//            spot =s;
//    }
//}
//spot.setOccupied(true);
//        if(numberOfWheels==2)
//            spot.setSpotType(SpotType.TWO_WHEELER);
//        else if(numberOfWheels==4 )
//            spot.setSpotType(SpotType.FOUR_WHEELER);
//        else if(numberOfWheels>4)
//            spot.setSpotType(SpotType.OTHERS);
//reservation.setSpot(spot);
//reservation.setUser(user);
//reservation.setNumberOfHours(timeInHours);
//if(spot==null)
//    throw new Exception("Cannot make reservation");
//reservationRepository3.save(reservation);
//return reservation;
    }
    public int spotTypeFunction(SpotType spotType){
        if (spotType==SpotType.TWO_WHEELER){
            return 2;
        }
        else if (spotType==SpotType.FOUR_WHEELER) {
            return 4;
        }
        else return Integer.MAX_VALUE;
    }
}
