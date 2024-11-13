package com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils;

import java.util.ArrayList;
import java.util.Calendar;

public class ArrayListCalendar {

    public static ArrayListCalendar arrayListCalendar = new ArrayListCalendar();

    public ArrayList<Calendar> arrayListStart = new ArrayList<>();
    public ArrayList<Calendar> arrayListEND = new ArrayList<>();

//    public ArrayList<Calendar> hiredStart = new ArrayList<>();
//    public ArrayList<Calendar> hiredEND = new ArrayList<>();
//
//    public ArrayList<Calendar> arrayToolsStart = new ArrayList<>();
//    public ArrayList<Calendar> arrayToolsEND = new ArrayList<>();

    public Calendar startAvaliable = Calendar.getInstance();
    public Calendar endAvaliable = Calendar.getInstance();


//    static {
//        DetailsTO detailsTO = new DetailsTO();
//        detailsTO.setTradeID("20190508112536421");
//        detailsTO.setLoginUserID("cd680e79-11c4-4845-afdd-b5fc06ec7541");
//
//        ServiceAPI serviceAPI = RetrofitProvider.getAPI();
//        Call<DetailsTO> call = serviceAPI.getDetailsTrades(detailsTO);
//        call.enqueue(new Callback<DetailsTO>() {
//            @Override
//            public void onResponse(Call<DetailsTO> call, Response<DetailsTO> response) {
//                if (!response.isSuccessful()) {
//                    return;
//                }
//                try {
//                    ArrayList<NotAvailableTO> notAvailableTOArrayList = response.body().getTradesTO().getNotAvailableTrades();
//                    String startDate = notAvailableTOArrayList.get(0).getsDate();
//                    String endDate = notAvailableTOArrayList.get(0).geteDate();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = sdf.parse(startDate);
//                    Calendar calendar = Calendar.getInstance();
//                    Calendar calendarStart1 = Calendar.getInstance();
//                    calendarStart1.setTime(date);
//                    arrayListStart.add(calendarStart1);
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DetailsTO> call, Throwable t) {
//
//            }
//        });
//    }

         static
    {


        Calendar calendarStart1 = Calendar.getInstance();
        calendarStart1.set(2019, 4, 7);
        Calendar calendarStart2 = Calendar.getInstance();
        calendarStart2.set(2019, 4, 15);
        Calendar calendarStart3 = Calendar.getInstance();
        calendarStart3.set(2019, 4, 23);


        Calendar calendarEND1 = Calendar.getInstance();
        calendarEND1.set(2019,4,8);
        Calendar calendarEND2 = Calendar.getInstance();
        calendarEND2.set(2019,4,16);
        Calendar calendarEND3 = Calendar.getInstance();
        calendarEND3.set(2019,4,24);

//        arrayListStart.add(calendarStart1);
//        arrayListStart.add(calendarStart2);
//        arrayListStart.add(calendarStart3);

//        arrayListEND.add(calendarEND1);
//        arrayListEND.add(calendarEND2);
//        arrayListEND.add(calendarEND3);

    }
    public void end() {

    }

}
