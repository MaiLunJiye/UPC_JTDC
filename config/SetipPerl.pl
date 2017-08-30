#!/usr/bin/perl

use utf8;
use strict;
use 5.010;
use JSON;
use Data::Dumper;

my ( $JSONpath, $dev, $ipgroup, ) = @ARGV;

open my $Conft, '<', $JSONpath or die $!;
my $jsonString;
$jsonString .= $_ while ( $_ = <$Conft> );
close $Conft;

my $jsonObj = decode_json $jsonString;
die "can't find ipgroup $ipgroup, $!" unless $jsonObj->{$ipgroup};

my $netmask = "255.255.255.0";    # 子网掩码
my $index   = 0;
for my $ip ( @{ $jsonObj->{$ipgroup} } ) {
    system("ifconfig $dev:$index $ip netmask $netmask up");
    $index++;
    say $ip;
}

#system("ifconfig $dev:$index $ip netmask $netmask up");

#my $ipbase='192.168.31.';       # 网络段
#my $end=9;                      # 分配数量（9 表示 分配10 个 0..9)

#my @wlanip;

#for my $index (0..$end) {
#my $ip = $ipbase.($ipstart + $index);
#push @wlanip,$ip;
##system("ifconfig ".$dev.":".$index." ".$ip." netmask ".$netmask." up");
##say "ifconfig $dev:$index $ip netmask $netmask up";
#system("ifconfig $dev:$index $ip netmask $netmask up");
#}

#for my $index (@wlanip) {
#say "\"$index\",";
#}

#say "-----------------------";

#for my $index (@wlanip2) {
#say "\"$index\",";
#}
