package com.example.demo.importer;

import java.util.UUID;

public class importBase {
   private UUID uuid;
   
   public importBase(UUID u)
   {
	   this.uuid = u;
   }
   
   public UUID getUuid() { return this.uuid; }
   public void setUuid(UUID uuid) { this.uuid = uuid; }
}
