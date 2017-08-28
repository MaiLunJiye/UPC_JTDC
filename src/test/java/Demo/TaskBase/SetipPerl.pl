#!/usr/bin/perl
# 给网卡添加ip地址脚本

use utf8;
use 5.010;

my ($dev, $ipgroup) = @ARGV;

my $ipstart;
given ($ipgroup) {
    when($_ eq "ip1") {$ipstart = 150;}
    when($_ eq "ip2") {$ipstart = 170;}
    default {die "error input ipstart";}
};

my $ipbase='192.168.31.';       # 网络段
my $netmask = "255.255.255.0";  # 子网掩码
my $end=9;                      # 分配数量（9 表示 分配10 个 0..9)

my @wlanip;

for my $index (0..$end) {
    my $ip = $ipbase.($ipstart + $index);
    push @wlanip,$ip;
    #system("ifconfig ".$dev.":".$index." ".$ip." netmask ".$netmask." up");
    #say "ifconfig $dev:$index $ip netmask $netmask up";
    system("ifconfig $dev:$index $ip netmask $netmask up");
}

for my $index (@wlanip) {
    say "\"$index\",";
}

#say "-----------------------";

#for my $index (@wlanip2) {
    #say "\"$index\",";
#}
