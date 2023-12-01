//
//  EditCarView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import SwiftUI

struct EditCarView: View {
    @State var car: Car
    var onCarUpdated: () -> Void

    var body: some View {
        Form {
            TextField("Номер", text: $car.num)
            TextField("Цвет", text: $car.color)
            TextField("Марка", text: $car.mark)
            Toggle("Импортный", isOn: $car.foreign)
            Button("Обновить") {
                updateCar()
            }
        }
        .navigationTitle("Редактировать Автомобиль")
    }

    private func updateCar() {
        guard let carId = car.id else {
            print("Car ID is missing")
            return
        }

        CarNetworkManager.shared.updateCar(carId: carId, updatedCar: car) { success in
            if success {
                print("Автомобиль успешно обновлен")
                onCarUpdated() // Обновление списка автомобилей в родительском виде
            } else {
                print("Ошибка при обновлении автомобиля")
            }
        }
    }
}

