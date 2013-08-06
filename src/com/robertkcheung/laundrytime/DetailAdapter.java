package com.robertkcheung.laundrytime;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailAdapter extends ArrayAdapter<Machine> {
	DetailHall dh;
	Context ctx;
	int resID;
	Vector<Machine> machineList;
	boolean isWasher;
	
	
	public DetailAdapter(Context context, int textViewResourceId, DetailHall detailhall, boolean wash) {
		
		super(context, textViewResourceId,detailhall.getMachines());
		dh = detailhall;
		ctx = context;
		resID = textViewResourceId;
		machineList = dh.getMachines();
		isWasher = wash;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// check for if view is new
	    if (convertView == null) {
	        // This a new view; inflate the new layout
	        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = inflater.inflate(resID, parent, false);
	    }
	        // fill the layout with the right values
	        TextView machineNumber = (TextView) convertView.findViewById(R.id.detailMachineNumber);
	        TextView time = (TextView) convertView.findViewById(R.id.detailTimeRemaining);
	        ImageView status = (ImageView) convertView.findViewById(R.id.detailStatus);

	        TextView fetch = (TextView) convertView.findViewById(R.id.detail_fetch);
	        TextView avail = (TextView) convertView.findViewById(R.id.detailAvailable);
	        TextView justfinished = (TextView) convertView.findViewById(R.id.detail_just_finished);
	        ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.detailProgress);
	        TextView inuse = (TextView) convertView.findViewById(R.id.detailinUse);
	        TextView notresponding = (TextView) convertView.findViewById(R.id.detailNotResponding);
	        
	        Machine currMachine = machineList.get(position);
	        String timeLeft = currMachine.getTime();
	        String statustxt = currMachine.getStatus();
	        
	        /*
	         * -1: just finished
	         * -2: available
	         * -3: error
	         * 
	         * 
	         */
	        //if(isWasher)
	        	//machineNumber.setTextColor(ctx.getResources().getColor(R.color.sudsblue));
	        //else
	        	//machineNumber.setTextColor(ctx.getResources().getColor(R.color.salmon));
	        machineNumber.setText(currMachine.getMachineNumber()+"");
	        int left=0;
	        try{
	        	if(timeLeft.contains("ago") || timeLeft.contains("just")){
	        		try{
	        		left = Integer.parseInt(timeLeft.substring(0, timeLeft.indexOf(" ")));
	        		}catch(Exception parseexception){
	        			left = 0;
	        		}
	        		notresponding.setVisibility(View.GONE);
	        		justfinished.setVisibility(View.VISIBLE);
	        		fetch.setVisibility(View.GONE);
	        		avail.setVisibility(View.GONE);
	        		progress.setVisibility(View.GONE);
	        		inuse.setVisibility(View.GONE);
	        		time.setVisibility(View.VISIBLE);
	        		time.setText(left + " MINUTES AGO");
	        		status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.yellowdot));
	        		
	        	}
	        	else if(statustxt.contains("Available")){
	        		notresponding.setVisibility(View.GONE);
	        		justfinished.setVisibility(View.GONE);
	        		fetch.setVisibility(View.GONE);
	        		avail.setVisibility(View.VISIBLE);
	        		progress.setVisibility(View.GONE);
	        		inuse.setVisibility(View.GONE);
	        		time.setVisibility(View.GONE);
	        		status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.greendot));
	        	}
	        	else if(timeLeft.contains("not updating")){
	        		notresponding.setVisibility(View.VISIBLE);
	        		justfinished.setVisibility(View.GONE);
		        	inuse.setVisibility(View.GONE);
	        		fetch.setVisibility(View.GONE);
	        		avail.setVisibility(View.GONE);
	        		progress.setVisibility(View.GONE);
		        	time.setVisibility(View.GONE);
		        	//time.setText("(MACHINE MIGHT NOT EXIST)");
		        	status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.graydot));
	        	}
	        	else{
	        		notresponding.setVisibility(View.GONE);
	        		justfinished.setVisibility(View.GONE);
	        		fetch.setVisibility(View.GONE);
	        		avail.setVisibility(View.GONE);
	        		inuse.setVisibility(View.VISIBLE);
	        		if(statustxt.contains("Add'l") || statustxt.contains("Time"))
	        			inuse.setText("ADDED TIME");
	        		progress.setVisibility(View.VISIBLE);
	        		left = Integer.parseInt(timeLeft.substring(0, timeLeft.indexOf(" ")));
	        		if(isWasher && left>45){
	        			progress.setMax(left);
	        		}
	        		else if(isWasher)
	        			progress.setMax(45);
	        		else if(!isWasher && left>60)
	        			progress.setMax(left);
	        		else if(!isWasher)
	        			progress.setMax(60);
		        	progress.setProgress(left);
		        	time.setVisibility(View.VISIBLE);
		        	time.setText(left + " MINUTES REMAINING");
		        	status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.reddot));
	        	}
	        }catch(Exception e){
	        	e.printStackTrace();
	        	notresponding.setVisibility(View.GONE);
	        	justfinished.setVisibility(View.GONE);
	        	inuse.setVisibility(View.GONE);
        		fetch.setVisibility(View.VISIBLE);
        		avail.setVisibility(View.GONE);
        		progress.setVisibility(View.GONE);
	        	time.setVisibility(View.GONE);
	        	status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.graydot));
	        }
	        
	        
	        
	        
	     
	    return convertView;
		
	}

}
