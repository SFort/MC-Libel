package com.unascribed.libel.interfaces;

import net.minecraft.nbt.NBTTagList;

public interface SetCanHitList {

	void fabrication$setCanHitLists(NBTTagList list, NBTTagList list2);
	NBTTagList fabrication$getCanHitList();
	NBTTagList fabrication$getCanHitList2();

}
