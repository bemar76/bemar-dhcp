# bemar-dhcp
A dhcp server compatible to dhcpd.conf


## Currently supported
- subnet
- host config
- basic and common dhcp options

## Currently not supported
- zone
- group
- subclass
- log-facility
- failover 
- shared-network
- class 
- ddns-*
- update-static-leases

## more dhcp options and configurations will follow

## Example
```json

# common config
option domain-name "bemar.local";
option domain-name-servers 8.8.8.8, 8.8.4.4;

default-lease-time 400;
max-lease-time 3600;

authoritative;

# Subnet config
subnet 192.168.64.0 netmask 255.255.255.0 {
    
    # range definition
    range 192.168.64.100 192.168.64.200;

    # Standard-Gateway
    option routers 192.168.64.1;

    # DNS-Server
    option domain-name-servers 8.8.8.8, 8.8.4.4;

    # Lease-Time (optional)
    default-lease-time 600;
    max-lease-time 7200;

    # static IP for host
    host my-pc {
        hardware ethernet 8D:FF:17:02:4E:3E;
        fixed-address 192.168.64.98;
    }
}
```