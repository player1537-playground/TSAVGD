class Out
{
  void init() 
  {
     EventTest.addEntity((new Person()
    {
      void init() 
      {
        this.setModel("martha.obj");
        this.setPosition(0.5,1.0,3.1);
        this.setConversation("martha.conv");
        
      }
      
    }
    ));
    EventTest.addEntity((new Person()
    {
      void init() 
      {
        this.setModel("sebastiin.obj");
        this.setPosition(0.1,3.1,66.6);
        this.setConversation("sebastiin.conv");
        
      }
      
    }
    ));
    EventTest.setTerrainModel("island-level.obj");
    EventTest.setCollideModel("island-level-col.obj");
    EventTest.setPlaylist((new Song[] 
    {
        "ding.wav","jazz.wav","ding.wav" 
    }
    ),5000);
    
  }
  
}

