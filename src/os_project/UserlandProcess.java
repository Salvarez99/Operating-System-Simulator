package os_project;

public abstract class UserlandProcess implements Runnable {

    private static int[][] TLB = new int[2][2];

    private static byte[] physicalMemory = new byte[(1024*1024)];

    byte read(int virtualAddress){
        /*
		 * TODO:
         * Find page number
         * pageNum = address / page size
         * Search TLB to see if virtual -> physical mapping exist
         * If so:
         *      physical address = physical page # * page size + page offset
         *      return byte from mem array
         * else:
         *      call getMapping()
         */

        int pageSize = 1024;
        int virtualPageNumber = virtualAddress / pageSize;
        int pageOffset = virtualAddress % pageSize;
        boolean found = false;

        while(!found){
            for (int row = 0; row < TLB.length; row++) {
                if(TLB[row][0] == virtualPageNumber){
                    int physicalPageNumber = TLB[row][0];
                    int physicalAddress = physicalPageNumber * pageSize + pageOffset;
                    found = true;
                    return physicalMemory[physicalAddress];
                }
            }
            
            if(found){
                break;
            }
            OS.getMapping(virtualPageNumber);
        }
        return -1;
    }

    void write(int virtualAddress, byte value){
        /*
		 * TODO:
         * Find page number
         * pageNum = address / page size
         * Search TLB to see if virtual -> physical mapping exist
         * If so:
         *      physical address = physical page # * page size + page offset
         *      write in physical address
         * else:
         *      call getMapping()
         */
        
        int pageSize = 1024;
        int virtualPageNumber = virtualAddress / pageSize;
        int pageOffset = virtualAddress % pageSize;
        boolean found = false;

        while(!found){
            for (int row = 0; row < TLB.length; row++) {
                if(TLB[row][0] == virtualPageNumber){
                    int physicalPageNumber = TLB[row][0];
                    int physicalAddress = physicalPageNumber * pageSize + pageOffset;
                    found = true;
                    physicalMemory[physicalAddress] = value;
                }
            }
            
            if(found){
                break;
            }
            OS.getMapping(virtualPageNumber);
        }
    }

    public static int[][] getTLB() {
        return TLB;
    }

    public static void setTLB(int[][] tLB) {
        TLB = tLB;
    }
    
}
