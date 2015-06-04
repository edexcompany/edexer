package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.edexer.model.ActAs;
import com.edexer.model.UserSubscription;
import com.edexer.service.ActAsServiceManager;

@ManagedBean(name="actAsBean", eager=true)
@ApplicationScoped
public class ActAsManagedBean {

	private ActAs actAs;
	
	@ManagedProperty("#{actAsServiceManager}")
	private ActAsServiceManager actAsManager;
	
	private List<ActAs> actAsList;
	
	
	public List<ActAs> getActAsList(){
		if(actAsList == null){
			actAsList = actAsManager.getActAsList();
		}
		return actAsList;
	}
	
	public List<ActAs> getActAsList2(UserSubscription userSub){
		if(actAsList == null){
			actAsList = actAsManager.getActAsList();
		}
		orderListBasedOnUserSubscription(userSub.getActAs());
		return actAsList;
	}
	
	private void orderListBasedOnUserSubscription(ActAs actAs){
		int objIndex=0;
		boolean isEqualActAs;
		for(ActAs a : actAsList){
			isEqualActAs = a.getActAsId() == actAs.getActAsId();
			if(isEqualActAs )
			{
				if(objIndex == 0){ // this is a small enhancement, to avoid removal and insertion, in case that the 1st act as object is the one to be removed.
					return;
				}
				break;
			}
			objIndex++;
		}
		actAsList.remove(objIndex);
		actAsList.add(0, actAs);
	}
	
	public void setActAsList(List<ActAs> l){
		actAsList = l;
	}

	public ActAsServiceManager getActAsManager() {
		return actAsManager;
	}

	public void setActAsManager(ActAsServiceManager actAsManager) {
		this.actAsManager = actAsManager;
	}

	public ActAs getActAs() {
		return actAs;
	}

	public void setActAs(ActAs actAs) {
		this.actAs = actAs;
	}
	
}
