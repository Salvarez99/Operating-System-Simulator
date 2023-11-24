package os_project;

public class VirtualToMemoryMapping {
    public int physical_page_number;
    public int on_disk_page_number;

    public VirtualToMemoryMapping(){
        this.on_disk_page_number = -1;
        this.physical_page_number = -1;
    }

    public VirtualToMemoryMapping(int physical_page_number, int on_disk_page_number){
        this.on_disk_page_number = on_disk_page_number;
        this.physical_page_number = physical_page_number;
    }

    public int getPhysical_page_number() {
        return physical_page_number;
    }

    public void setPhysical_page_number(int physical_page_number) {
        this.physical_page_number = physical_page_number;
    }

    public int getOn_disk_page_number() {
        return on_disk_page_number;
    }

    public void setOn_disk_page_number(int on_disk_page_number) {
        this.on_disk_page_number = on_disk_page_number;
    }
}