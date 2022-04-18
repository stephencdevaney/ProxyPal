//CREATED BY BLAKE

package com.example.ppstart;

public class MessagesClass {
   private String owner_username;
   private String supporter_username;
   private int owner_id;
   private int supporter_id;
   private String viewer_username;
   private String message;

   public MessagesClass() {
   }

   public MessagesClass(String owner_username, String supporter_username, int owner_id, int supporter_id, String viewer_username, String message) {
      this.owner_username = owner_username;
      this.supporter_username = supporter_username;
      this.owner_id = owner_id;
      this.supporter_id = supporter_id;
      this.viewer_username = viewer_username;
      this.message = message;
   }

   public String getOwner_username() {
      return owner_username;
   }

   public void setOwner_username(String owner_username) {
      this.owner_username = owner_username;
   }

   public String getSupporter_username() {
      return supporter_username;
   }

   public void setSupporter_username(String supporter_username) {
      this.supporter_username = supporter_username;
   }

   public int getOwner_id() {
      return owner_id;
   }

   public void setOwner_id(int owner_id) {
      this.owner_id = owner_id;
   }

   public int getSupporter_id() {
      return supporter_id;
   }

   public void setSupporter_id(int supporter_id) {
      this.supporter_id = supporter_id;
   }

   public String getViewer_username() {
      return viewer_username;
   }

   public void setViewer_username(String viewer_username) {
      this.viewer_username = viewer_username;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
