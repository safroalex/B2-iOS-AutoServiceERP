//
//  TopMastersModel.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//
import Foundation

struct MasterStatistic: Codable, Identifiable {
    var id: Int
    var name: String
    var numberOfWorks: Int
}

